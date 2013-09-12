package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.server.security.SkysailRoleAuthorizer;

public class RouteBuilder {

    private final String pathTemplate;
    private Class<? extends ServerResource> targetClass;
    private String text = null;
    private boolean visible = true;
    private Restlet restlet;
    private final List<SkysailRoleAuthorizer> rolesAuthorizers = new ArrayList<SkysailRoleAuthorizer>();

    public RouteBuilder(String pathTemplate, Class<? extends ServerResource> targetClass) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(targetClass, "targetClass may not be null");
        this.pathTemplate = pathTemplate;
        this.targetClass = targetClass;
    }

    public RouteBuilder(String pathTemplate, Restlet restlet) {
        Validate.notNull(pathTemplate, "pathTemplate may not be null");
        Validate.notNull(restlet, "target may not be null");
        this.pathTemplate = pathTemplate;
        this.restlet = restlet;
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
        for (SkysailRoleAuthorizer authorizer : rolesAuthorizers) {
            sb.append(" ").append(authorizer);
        }
        sb.append(" (visible:").append(visible);
        sb.append(")");
        return sb.toString();
    }

    public Class<? extends ServerResource> getTargetClass() {
        return targetClass;
    }

    public Restlet getRestlet() {
        return restlet;
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

    public RouteBuilder setSecuredByRole(String roleName) {
        SkysailRoleAuthorizer authorizer = new SkysailRoleAuthorizer(roleName);
        rolesAuthorizers.add(authorizer);
        authorizer.setNext(targetClass);
        targetClass = null;
        restlet = authorizer;
        return this;
    }

    public List<SkysailRoleAuthorizer> getRolesAuthorizers() {
        return Collections.unmodifiableList(rolesAuthorizers);
    }
}
