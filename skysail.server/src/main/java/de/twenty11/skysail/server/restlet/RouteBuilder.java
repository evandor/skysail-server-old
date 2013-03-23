package de.twenty11.skysail.server.restlet;

import org.apache.commons.lang.Validate;
import org.restlet.resource.ServerResource;

public class RouteBuilder {

    private String pathTemplate;
    private Class<? extends ServerResource> targetClass;
    private String text = "";
    private boolean visible = true;

    public RouteBuilder(String pathTemplate, Class<? extends ServerResource> targetClass) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(targetClass, "targetClass may not be null");
        this.pathTemplate = pathTemplate;
        this.targetClass = targetClass;
    }

    public RouteBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public RouteBuilder setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pathTemplate).append(" -> ").append(targetClass);
        sb.append(" (visible:").append(visible);
        sb.append(")");
        return sb.toString();
    }

    public Class<? extends ServerResource> getTargetClass() {
        return targetClass;
    }

    public String getPathTemplate() {
        return pathTemplate;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getText() {
        return text;
    }
}
