package de.twenty11.skysail.server.directory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.engine.util.AlphaNumericComparator;
import org.restlet.engine.util.AlphabeticalComparator;
import org.restlet.representation.Variant;
import org.restlet.resource.Finder;

import de.twenty11.skysail.common.responses.SkysailResponse;

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
        this.modifiable = false;
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

    public SkysailResponse getIndexRepresentation(Variant variant, SkysailReferenceList indexContent) {
        SkysailResponse result = null;
        // if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
        // result = indexContent.getWebRepresentation();
        // } else if (variant.getMediaType().equals(MediaType.TEXT_URI_LIST)) {
        // result = indexContent.getTextRepresentation();
        // } else if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
        result = indexContent.getJsonVariant();
        // }

        return result;
    }

    public List<Variant> getIndexVariants(SkysailReferenceList indexContent) {
        final List<Variant> result = new ArrayList<Variant>();
        result.add(new Variant(MediaType.APPLICATION_JSON));
        // result.add(new Variant(MediaType.TEXT_HTML));
        // result.add(new Variant(MediaType.TEXT_URI_LIST));
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
