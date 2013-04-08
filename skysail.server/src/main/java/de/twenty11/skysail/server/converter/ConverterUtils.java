package de.twenty11.skysail.server.converter;

import org.restlet.resource.Resource;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;

public class ConverterUtils {

    public static PresentationStyle evalPresentationStyle(Resource resource) {
        PresentationStyle style = PresentationStyle.LIST;
        if (resource.getClass().isAnnotationPresent(Presentation.class)) {
            Presentation annotation = resource.getClass().getAnnotation(Presentation.class);
            style = annotation.preferred();
        }
        return style;
    }
}
