package de.twenty11.skysail.server.restlet.mediatype;

import org.restlet.data.Metadata;

public class HtmlFormMediaType extends Metadata {

    public static final String SKYSAIL_MEDIATYPE_HTMLFORM = "htmlform";

    public HtmlFormMediaType() {
        super(SKYSAIL_MEDIATYPE_HTMLFORM);
    }

    @Override
    public Metadata getParent() {
        return null;
    }

    @Override
    public boolean includes(Metadata included) {
        return false;
    }

}
