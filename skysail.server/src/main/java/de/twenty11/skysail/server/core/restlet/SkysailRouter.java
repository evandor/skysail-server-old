package de.twenty11.skysail.server.core.restlet;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Context;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.security.Authorizer;

import de.twenty11.skysail.server.security.AuthorizationService;

public class SkysailRouter extends Router {

    private Map<String, RouteBuilder> routes = new ConcurrentHashMap<String, RouteBuilder>();
    private AuthorizationService authorizationService;

    public SkysailRouter(Context context, AuthorizationService authorizationService) {
        super(context);
        this.authorizationService = authorizationService;
    }

    public void attach(RouteBuilder routeBuilder) {
        routes.put(routeBuilder.getPathTemplate(), routeBuilder);
        if (routeBuilder.getTargetClass() != null) {

            // if (routeBuilder.getSecuredByRole() != null) {
            Authorizer authorizer = authorizationService.getRoleAuthorizer(routeBuilder.getSecuredByRole());
            authorizer.setContext(getContext());
            // Authorizer authorizer = roleAuthorizerFactory.create(roleName);
            // rolesAuthorizers.add(authorizer);
            authorizer.setNext(routeBuilder.getTargetClass());
            attach(routeBuilder.getPathTemplate(), authorizer);
            // } else {
            // attach(routeBuilder.getPathTemplate(), routeBuilder.getTargetClass());
            // }
        } else {
            attach(routeBuilder.getPathTemplate(), routeBuilder.getRestlet());
        }
    }

    public RouteBuilder getRouteBuilder(String pathTemplate) {
        return routes.get(pathTemplate);
    }

    public Map<String, RouteBuilder> getRouteBuilders() {
        return Collections.unmodifiableMap(routes);
    }

    public String getTemplatePathForResource(Class<? extends ServerResource> cls) {
        for (Entry<String, RouteBuilder> entries : routes.entrySet()) {
            if (entries.getValue().getTargetClass().equals(cls)) {
                return entries.getKey();
            }
        }
        return null;
    }

}
