package de.twenty11.skysail.server.directory;

import java.io.IOException;
import java.util.List;

import org.restlet.data.Reference;
import org.restlet.data.ReferenceList;
import org.restlet.representation.Representation;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;

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

    public SkysailResponse<List<Reference>> getJsonVariant() {
        // for (final Reference ref : this) {
        // // .append("<a href=\"" + ref.toString() + "\">"
        // // + ref.getRelativeRef(getIdentifier()) + "</a><br>\n");
        // }
        return new SuccessResponse<List<Reference>>(this);
    }

}
