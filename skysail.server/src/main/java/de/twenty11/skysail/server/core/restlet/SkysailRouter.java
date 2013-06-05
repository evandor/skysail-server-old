package de.twenty11.skysail.server.core.restlet;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Context;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;

public class SkysailRouter extends Router {

    private Map<String, RouteBuilder> routes = new ConcurrentHashMap<String, RouteBuilder>();

    public SkysailRouter(Context context) {
        super(context);
    }

    public void attach(RouteBuilder routeBuilder) {
        routes.put(routeBuilder.getPathTemplate(), routeBuilder);
        if (routeBuilder.getTargetClass() != null) {
            attach(routeBuilder.getPathTemplate(), routeBuilder.getTargetClass());
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
