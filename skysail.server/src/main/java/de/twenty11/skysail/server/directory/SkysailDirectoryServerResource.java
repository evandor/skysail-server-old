package de.twenty11.skysail.server.directory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Preference;
import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.data.Status;
import org.restlet.engine.local.Entity;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class SkysailDirectoryServerResource extends ServerResource {

    private volatile String baseName;
    private volatile Variant baseVariant;
    private volatile SkysailDirectory directory;
    private volatile SkysailReferenceList directoryContent;
    private volatile boolean directoryRedirection;
    private volatile boolean directoryTarget;
    private volatile String directoryUri;
    private volatile Representation fileContent;
    private volatile boolean fileTarget;
    private volatile boolean indexTarget;
    private volatile Reference originalRef;
    private volatile Variant protoVariant;
    private volatile String relativePart;
    private volatile String targetUri;
    private volatile Reference uniqueReference;
    private volatile List<Variant> cachedVariantsForGet;

    @Override
    public void doInit() throws ResourceException {
        try {
            // Update the member variables
            this.directory = (SkysailDirectory) getRequestAttributes().get("org.restlet.directory");
            this.relativePart = getReference().getRemainingPart(false, false);
            setNegotiated(this.directory.isNegotiatingContent());

            // Restore the original URI in case the call has been tunneled.
            if ((getApplication() != null) && getApplication().getTunnelService().isExtensionsTunnel()) {
                restoreOriginalURIwhenCallwasTunneled();
            }

            if (this.relativePart.startsWith("/")) {
                // We enforce the leading slash on the root URI
                this.relativePart = this.relativePart.substring(1);
            }

            // The target URI does not take into account the query and fragment
            // parts of the resource.
            this.targetUri = new Reference(directory.getRootRef().toString() + this.relativePart).normalize().toString(
                    false, false);
            if (!this.targetUri.startsWith(directory.getRootRef().toString())) {
                // Prevent the client from accessing resources in upper
                // directories
                this.targetUri = directory.getRootRef().toString();
            }

            if (getClientDispatcher() == null) {
                getLogger()
                        .warning(
                                "No client dispatcher is available on the context. Can't get the target URI: "
                                        + this.targetUri);
            } else {
                // Try to detect the presence of a directory
                Response contextResponse = getRepresentation(this.targetUri);

                if (contextResponse.getEntity() != null) {
                    // As a convention, underlying client connectors return the
                    // directory listing with the media-type
                    // "MediaType.TEXT_URI_LIST" when handling directories
                    if (MediaType.TEXT_URI_LIST.equals(contextResponse.getEntity().getMediaType())) {
                        this.directoryTarget = true;
                        this.fileTarget = false;
                        this.directoryContent = new SkysailReferenceList(contextResponse.getEntity());

                        if (!getReference().getPath().endsWith("/")) {
                            // All requests will be automatically redirected
                            this.directoryRedirection = true;
                        }

                        if (!this.targetUri.endsWith("/")) {
                            this.targetUri += "/";
                            this.relativePart += "/";
                        }
                        appendIndexName();
                    } else {
                        // Allows underlying helpers that do not support
                        // "content negotiation" to return the targeted file.
                        // Sometimes we immediately reach the target entity, so
                        // we return it directly.
                        this.directoryTarget = false;
                        this.fileTarget = true;
                        this.fileContent = contextResponse.getEntity();
                    }
                } else {
                    this.directoryTarget = false;
                    this.fileTarget = false;

                    // Let's try with the optional index, in case the underlying
                    // client connector does not handle directory listing.
                    if (this.targetUri.endsWith("/")) {
                        // In this case, the trailing "/" shows that the URI
                        // must point to a directory
                        if ((getDirectory().getIndexName() != null) && (getDirectory().getIndexName().length() > 0)) {
                            this.directoryUri = this.targetUri;
                            this.directoryTarget = true;

                            contextResponse = getRepresentation(this.directoryUri + getDirectory().getIndexName());
                            if (contextResponse.getEntity() != null) {
                                this.baseName = getDirectory().getIndexName();
                                this.targetUri = this.directoryUri + this.baseName;
                                this.directoryContent = new SkysailReferenceList();
                                this.directoryContent.add(new Reference(this.targetUri));
                                this.indexTarget = true;
                            }
                        }
                    } else {
                        // Try to determine if this target URI with no trailing
                        // "/" is a directory, in order to force the
                        // redirection.
                        if ((getDirectory().getIndexName() != null) && (getDirectory().getIndexName().length() > 0)) {
                            // Append the index name
                            appendIndexname2();
                        }
                    }
                }

                // In case the request does not target a directory and the file
                // has not been found, try with the tunneled URI.
                if (isNegotiated() && !this.directoryTarget && !this.fileTarget && (this.originalRef != null)) {
                    this.relativePart = getReference().getRemainingPart();

                    // The target URI does not take into account the query and
                    // fragment parts of the resource.
                    this.targetUri = new Reference(directory.getRootRef().toString() + this.relativePart).normalize()
                            .toString(false, false);
                    if (!this.targetUri.startsWith(directory.getRootRef().toString())) {
                        // Prevent the client from accessing resources in upper
                        // directories
                        this.targetUri = directory.getRootRef().toString();
                    }
                }

                if (!fileTarget || (fileContent == null) || !getRequest().getMethod().isSafe()) {
                    // Try to get the directory content, in case the request
                    // does not target a directory
                    if (!this.directoryTarget) {
                        int lastSlashIndex = this.targetUri.lastIndexOf('/');
                        if (lastSlashIndex == -1) {
                            this.directoryUri = "";
                            this.baseName = this.targetUri;
                        } else {
                            this.directoryUri = this.targetUri.substring(0, lastSlashIndex + 1);
                            this.baseName = this.targetUri.substring(lastSlashIndex + 1);
                        }

                        contextResponse = getRepresentation(this.directoryUri);
                        if ((contextResponse.getEntity() != null)
                                && MediaType.TEXT_URI_LIST.equals(contextResponse.getEntity().getMediaType())) {
                            this.directoryContent = new SkysailReferenceList(contextResponse.getEntity());
                        }
                    }

                    if (this.baseName != null) {
                        // Analyze extensions
                        analyzeExtensions();
                    }

                    checkIfResourceExists();
                }
            }

            // Log results
            getLogger().fine("Converted target URI: " + this.targetUri);
            getLogger().fine("Converted base name : " + this.baseName);
        } catch (IOException ioe) {
            throw new ResourceException(ioe);
        }
    }

    @Override
    protected Representation get() throws ResourceException {
        List<Variant> variants = getVariants(Method.GET);
        if ((variants == null) || (variants.isEmpty())) {
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return null;
        }
        if (variants.size() == 1) {
            return (Representation) variants.get(0);
        }
        ReferenceList variantRefs = handleMultipleVariants(variants);
        if (variantRefs.size() > 0) {
            return returnListOfVariants(variantRefs);
        }
        setStatus(Status.CLIENT_ERROR_NOT_FOUND);
        return null;
    }

    private void appendIndexname2() {
        Response contextResponse;
        contextResponse = getRepresentation(this.targetUri + "/" + getDirectory().getIndexName());
        if (contextResponse.getEntity() != null) {
            this.directoryUri = this.targetUri + "/";
            this.baseName = getDirectory().getIndexName();
            this.targetUri = this.directoryUri + this.baseName;
            this.directoryTarget = true;
            this.directoryRedirection = true;
            this.directoryContent = new SkysailReferenceList();
            this.directoryContent.add(new Reference(this.targetUri));
            this.indexTarget = true;
        }
    }

    private void appendIndexName() {
        if ((getDirectory().getIndexName() != null) && (getDirectory().getIndexName().length() > 0)) {
            this.directoryUri = this.targetUri;
            this.baseName = getDirectory().getIndexName();
            this.targetUri = this.directoryUri + this.baseName;
            this.indexTarget = true;
        } else {
            this.directoryUri = this.targetUri;
            this.baseName = null;
        }
    }

    private void analyzeExtensions() {
        this.baseVariant = new Variant();
        Entity.updateMetadata(this.baseName, this.baseVariant, true, getMetadataService());
        this.protoVariant = new Variant();
        Entity.updateMetadata(this.baseName, this.protoVariant, false, getMetadataService());

        // Remove stored extensions from the base name
        this.baseName = Entity.getBaseName(this.baseName, getMetadataService());
    }

    private void restoreOriginalURIwhenCallwasTunneled() {
        this.originalRef = getOriginalRef();

        if (this.originalRef != null) {
            this.originalRef.setBaseRef(getReference().getBaseRef());
            this.relativePart = this.originalRef.getRemainingPart();
        }
    }

    private Representation returnListOfVariants(ReferenceList variantRefs) {
        // Return the list of variants
        setStatus(Status.REDIRECTION_MULTIPLE_CHOICES);
        return variantRefs.getTextRepresentation();
    }

    private ReferenceList handleMultipleVariants(List<Variant> variants) {
        ReferenceList variantRefs = new ReferenceList();

        for (Variant variant : variants) {
            if (variant.getLocationRef() != null) {
                variantRefs.add(variant.getLocationRef());
            } else {
                getLogger()
                        .warning(
                                "A resource with multiple variants should provide a location for each variant when content negotiation is turned off");
            }
        }
        return variantRefs;
    }

    /**
     * Returns the local base name of the file. For example, "foo.en" and "foo.en-GB.html" return "foo".
     * 
     * @return The local name of the file.
     */
    public String getBaseName() {
        return this.baseName;
    }

    /**
     * Returns a client dispatcher.
     * 
     * @return A client dispatcher.
     */
    protected Restlet getClientDispatcher() {
        return getDirectory().getContext() == null ? null : getDirectory().getContext().getClientDispatcher();
    }

    /**
     * Returns the parent directory handler.
     * 
     * @return The parent directory handler.
     */
    public SkysailDirectory getDirectory() {
        return this.directory;
    }

    /**
     * If the resource is a directory, this returns its content.
     * 
     * @return The directory content.
     */
    protected ReferenceList getDirectoryContent() {
        return directoryContent;
    }

    /**
     * Returns the context's directory URI (file, clap URI).
     * 
     * @return The context's directory URI (file, clap URI).
     */
    public String getDirectoryUri() {
        return this.directoryUri;
    }

    /**
     * Returns a representation of the resource at the target URI. Leverages the client dispatcher of the parent
     * directory's context.
     * 
     * @param resourceUri
     *            The URI of the target resource.
     * @return A response with the representation if success.
     */
    private Response getRepresentation(String resourceUri) {
        return getClientDispatcher().handle(new Request(Method.GET, resourceUri));
    }

    /**
     * Returns a representation of the resource at the target URI. Leverages the client dispatcher of the parent
     * directory's context.
     * 
     * @param resourceUri
     *            The URI of the target resource.
     * @param acceptedMediaType
     *            The accepted media type or null.
     * @return A response with the representation if success.
     */
    protected Response getRepresentation(String resourceUri, MediaType acceptedMediaType) {
        if (acceptedMediaType == null) {
            return getClientDispatcher().handle(new Request(Method.GET, resourceUri));
        }

        Request request = new Request(Method.GET, resourceUri);
        request.getClientInfo().getAcceptedMediaTypes().add(new Preference<MediaType>(acceptedMediaType));
        return getClientDispatcher().handle(request);
    }

    /**
     * Allows to sort the list of representations set by the resource.
     * 
     * @return A Comparator instance imposing a sort order of representations or null if no special order is wanted.
     */
    private Comparator<Representation> getRepresentationsComparator() {
        // Sort the list of representations by their identifier.
        Comparator<Representation> identifiersComparator = new Comparator<Representation>() {
            public int compare(Representation rep0, Representation rep1) {
                boolean bRep0Null = (rep0.getLocationRef() == null);
                boolean bRep1Null = (rep1.getLocationRef() == null);

                if (bRep0Null && bRep1Null) {
                    return 0;
                }
                if (bRep0Null) {
                    return -1;
                }

                if (bRep1Null) {
                    return 1;
                }

                return rep0.getLocationRef().getLastSegment().compareTo(rep1.getLocationRef().getLastSegment());
            }
        };
        return identifiersComparator;
    }

    /**
     * Returns the context's target URI (file, clap URI).
     * 
     * @return The context's target URI (file, clap URI).
     */
    public String getTargetUri() {
        return this.targetUri;
    }

    @Override
    public List<Variant> getVariants() {
        return getVariants(getMethod());
    }

    @Override
    protected List<Variant> getVariants(Method method) {
        if (!(Method.GET.equals(method) || Method.HEAD.equals(method))) {
            return null;
        }
        if (cachedVariantsForGet != null) {
            return cachedVariantsForGet;
        }
        if ((this.directoryContent != null) && (getReference() != null) && (getReference().getBaseRef() != null)) {
            this.cachedVariantsForGet = handleCaseA(null);
        } else if (this.fileTarget && (this.fileContent != null)) {
            this.cachedVariantsForGet = handleCaseB();
        }
        return this.cachedVariantsForGet;
    }

    private List<Variant> handleCaseB() {
        List<Variant> result;
        // Sets the location of the target representation.
        if (getOriginalRef() != null) {
            this.fileContent.setLocationRef(getRequest().getOriginalRef());
        } else {
            this.fileContent.setLocationRef(getReference());
        }

        result = new ArrayList<Variant>();
        result.add(this.fileContent);
        return result;
    }

    private List<Variant> handleCaseA(List<Variant> result) {
        // Allows to sort the list of representations
        SortedSet<Representation> resultSet = new TreeSet<Representation>(getRepresentationsComparator());

        // Compute the base reference (from a call's client point of
        // view)
        String baseRef = getReference().getBaseRef().toString(false, false);

        if (!baseRef.endsWith("/")) {
            baseRef += "/";
        }

        int lastIndex = this.relativePart.lastIndexOf("/");

        if (lastIndex != -1) {
            baseRef += this.relativePart.substring(0, lastIndex);
        }

        int rootLength = getDirectoryUri().length();

        if (this.baseName != null) {
            handleBasenameNotNull(resultSet, baseRef, rootLength);
        }

        if (!resultSet.isEmpty()) {
            result = new ArrayList<Variant>(resultSet);
        }

        if (resultSet.isEmpty()) {
            result = handleResultSetEmpty(result, baseRef, rootLength);
        }
        return result;
    }

    private List<Variant> handleResultSetEmpty(List<Variant> result, String baseRef, int rootLength) {
        if (this.directoryTarget && getDirectory().isListingAllowed()) {
            SkysailReferenceList userList = new SkysailReferenceList(this.directoryContent.size());
            // Set the list identifier
            userList.setIdentifier(baseRef);

            SortedSet<Reference> sortedSet = new TreeSet<Reference>(getDirectory().getComparator());
            sortedSet.addAll(this.directoryContent);

            for (Reference ref : sortedSet) {
                String filePart = ref.toString(false, false).substring(rootLength);
                StringBuilder filePath = new StringBuilder();
                if ((!baseRef.endsWith("/")) && (!filePart.startsWith("/"))) {
                    filePath.append('/');
                }
                filePath.append(filePart);
                userList.add(baseRef + filePath);
            }
            List<Variant> list = getDirectory().getIndexVariants(userList);
            for (Variant variant : list) {
                if (result == null) {
                    result = new ArrayList<Variant>();
                }

                result.add(getDirectory().getIndexRepresentation(variant, userList));
            }

        }
        return result;
    }

    private void handleBasenameNotNull(SortedSet<Representation> resultSet, String baseRef, int rootLength) {
        String filePath;
        for (Reference ref : getVariantsReferences()) {
            // Add the new variant to the result list
            Response contextResponse = getRepresentation(ref.toString());
            if (contextResponse.getStatus().isSuccess() && (contextResponse.getEntity() != null)) {
                filePath = ref.toString(false, false).substring(rootLength);
                Representation rep = contextResponse.getEntity();

                if (filePath.startsWith("/")) {
                    rep.setLocationRef(baseRef + filePath);
                } else {
                    rep.setLocationRef(baseRef + "/" + filePath);
                }

                resultSet.add(rep);
            }
        }
    }

    /**
     * Returns the references of the representations of the target resource according to the directory handler property
     * 
     * @return The list of variants references
     */
    private SkysailReferenceList getVariantsReferences() {
        SkysailReferenceList result = new SkysailReferenceList(0);

        try {
            this.uniqueReference = null;

            // Ask for the list of all variants of this resource
            Response contextResponse = getRepresentation(this.targetUri, MediaType.TEXT_URI_LIST);
            if (contextResponse.getEntity() != null) {
                // Test if the given response is the list of all variants for
                // this resource
                if (MediaType.TEXT_URI_LIST.equals(contextResponse.getEntity().getMediaType())) {
                    ReferenceList listVariants = new ReferenceList(contextResponse.getEntity());
                    String entryUri;
                    String fullEntryName;
                    String baseEntryName;
                    int lastSlashIndex;
                    int firstDotIndex;

                    for (Reference ref : listVariants) {
                        entryUri = ref.toString();
                        lastSlashIndex = entryUri.lastIndexOf('/');
                        fullEntryName = (lastSlashIndex == -1) ? entryUri : entryUri.substring(lastSlashIndex + 1);
                        baseEntryName = fullEntryName;

                        // Remove the extensions from the base name
                        firstDotIndex = fullEntryName.indexOf('.');
                        if (firstDotIndex != -1) {
                            baseEntryName = fullEntryName.substring(0, firstDotIndex);
                        }

                        // Check if the current file is a valid variant
                        if (baseEntryName.equals(this.baseName)) {
                            // Test if the variant is included in the base
                            // prototype variant
                            Variant variant = new Variant();
                            Entity.updateMetadata(fullEntryName, variant, true, getMetadataService());
                            if (this.protoVariant.includes(variant)) {
                                result.add(ref);
                            }

                            // Test if the variant is equal to the base variant
                            if (this.baseVariant.equals(variant)) {
                                // The unique reference has been found.
                                this.uniqueReference = ref;
                            }
                        }
                    }
                } else {
                    result.add(contextResponse.getEntity().getLocationRef());
                }
            }
        } catch (IOException ioe) {
            getLogger().log(Level.WARNING, "Unable to get resource variants", ioe);
        }

        return result;
    }

    @Get("html|json")
    public SkysailResponse handleGet() {
        List<Variant> variants2 = getVariants();
        return (SkysailResponse) variants2.get(0);
    }

    @Override
    public Representation handle() {
        Representation result = null;

        if (this.directoryRedirection) {
            if (this.originalRef != null) {
                if (this.originalRef.hasQuery()) {
                    redirectSeeOther(this.originalRef.getPath() + "/?" + this.originalRef.getQuery());
                } else {
                    redirectSeeOther(this.originalRef.getPath() + "/");
                }
            } else {
                if (getReference().hasQuery()) {
                    redirectSeeOther(getReference().getPath() + "/?" + getReference().getQuery());
                } else {
                    redirectSeeOther(getReference().getPath() + "/");
                }
            }
        } else {
            result = super.handle();
        }

        return result;
    }

    public boolean isDirectoryTarget() {
        return this.directoryTarget;
    }

    public boolean isFileTarget() {
        return this.fileTarget;
    }

    private void checkIfResourceExists() {
        List<Variant> variants = getVariants(Method.GET);
        if ((variants == null) || (variants.isEmpty())) {
            setExisting(false);
        }
    }

    /**
     * Sets the context's target URI (file, clap URI).
     * 
     * @param targetUri
     *            The context's target URI.
     */
    public void setTargetUri(String targetUri) {
        this.targetUri = targetUri;
    }
}
