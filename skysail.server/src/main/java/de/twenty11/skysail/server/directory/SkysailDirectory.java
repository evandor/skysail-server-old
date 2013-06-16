package de.twenty11.skysail.server.directory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.engine.resource.AnnotationInfo;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.engine.util.AlphaNumericComparator;
import org.restlet.engine.util.AlphabeticalComparator;
import org.restlet.representation.Variant;
import org.restlet.resource.Finder;

public class SkysailDirectory extends Finder {

    private volatile String indexName;
    private volatile boolean modifiable;
    private volatile Reference rootRef;
    private volatile Comparator<Reference> comparator;

    public SkysailDirectory(Context context, Reference rootLocalReference) {
        super(context);
        final String rootIdentifier = rootLocalReference.getTargetRef().getIdentifier();
        if (rootIdentifier.endsWith("/")) {
            this.rootRef = new Reference(rootIdentifier);
        } else {
            this.rootRef = new Reference(rootIdentifier + "/");
        }
        this.comparator = new AlphaNumericComparator();
        this.indexName = "index";
        this.modifiable = true;
        setTargetClass(SkysailDirectoryServerResource.class);
    }

    public SkysailDirectory(Context context, String rootUri) {
        this(context, new Reference(rootUri));
    }

    public Comparator<Reference> getComparator() {
        return this.comparator;
    }

    public String getIndexName() {
        return this.indexName;
    }

    public Variant getIndexRepresentation(Variant variant, SkysailReferenceList indexContent) {
        Variant result = null;
        AnnotationInfo annotationInfo = null;// new AnnotationInfo(this, Method.GET, javamethod, value);
        // if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
        // result = indexContent.getWebRepresentation();
        // } else if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
        // result = indexContent.getTextRepresentation();
        // } else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
        result = new VariantInfo(MediaType.APPLICATION_JSON, annotationInfo);// indexContent.getJsonVariant();
        // }

        return result;
    }

    public List<Variant> getIndexVariants(SkysailReferenceList indexContent) {
        final List<Variant> result = new ArrayList<Variant>();
        result.add(new Variant(MediaType.APPLICATION_JSON));
        return result;
    }

    public Reference getRootRef() {
        return this.rootRef;
    }

    @Override
    public void handle(Request request, Response response) {
        request.getAttributes().put("org.restlet.directory", this);
        super.handle(request, response);
    }

    // @Override
    // public void handle(Request request, Response response) {
    // super.handle(request, response);
    //
    // if (isStarted()) {
    //
    // request.getAttributes().put("org.restlet.directory", this);
    // SkysailDirectoryServerResource result = null;
    // try {
    // result = SkysailDirectoryServerResource.class.newInstance();
    // } catch (Exception e) {
    // getLogger().log(Level.WARNING, "Exception while instantiating the target server resource.", e);
    // }
    // if (result == null) {
    // response.setStatus(Status.CLIENT_ERROR_NOT_FOUND);
    // return;
    // }
    // result.init(getContext(), request, response);
    //
    // if ((response == null) || response.getStatus().isSuccess()) {
    // result.handle();
    // // response.setEntity(handleGet);
    // }
    // result.release();
    // }
    // }

    public boolean isListingAllowed() {
        return true;
    }

    public boolean isModifiable() {
        return this.modifiable;
    }

    public boolean isNegotiatingContent() {
        return true;
    }

    public void useAlphaComparator() {
        setComparator(new AlphabeticalComparator());
    }

    public void useAlphaNumComparator() {
        setComparator(new AlphabeticalComparator());
    }

    public void setComparator(Comparator<Reference> comparator) {
        this.comparator = comparator;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setModifiable(boolean modifiable) {
        this.modifiable = modifiable;
    }

    public void setRootRef(Reference rootRef) {
        this.rootRef = rootRef;
    }

}