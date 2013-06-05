package de.twenty11.skysail.server.directory;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.data.ReferenceList;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Representation;

public class SkysailReferenceList extends ReferenceList {

    public SkysailReferenceList(Representation entity) throws IOException {
        super(entity);
    }

    public SkysailReferenceList() {
        // TODO Auto-generated constructor stub
    }

    public SkysailReferenceList(int i) {
        super(i);
    }

    public VariantInfo getJsonVariant() {
        return new VariantInfo(MediaType.APPLICATION_JSON);
    }

}
