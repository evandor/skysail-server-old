package de.twenty11.skysail.server.presentation;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import de.twenty11.skysail.server.restlet.SkysailApplication;

public class IFrame2BootstrapConverter extends BootstrapHtmlConverter {

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
            result = 1.0F;
        } else {
            result = 0.5F;
        }
        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        float result = -1.0F;
        // if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
        // return 0.0F;
        // }
        // if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
        // result = 1.0F;
        // } else {
        // result = 0.5F;
        // }
        return result;
    }
}
