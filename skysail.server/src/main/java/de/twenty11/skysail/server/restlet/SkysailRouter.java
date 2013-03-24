package de.twenty11.skysail.server.restlet;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.restlet.Context;
import org.restlet.routing.Router;

public class SkysailRouter extends Router {


    private Map<String, RouteBuilder> routes = new ConcurrentHashMap<String, RouteBuilder>();

    public SkysailRouter(Context context) {
        super(context);
    }

    public void attach(RouteBuilder routeBuilder) {
        routes.put(routeBuilder.getPathTemplate(), routeBuilder);
        attach(routeBuilder.getPathTemplate(), routeBuilder.getTargetClass());
    }

    public RouteBuilder getRouteBuilder(String pathTemplate) {
        return routes.get(pathTemplate);
    }

    public Map<String, RouteBuilder> getRouteBuilders() {
        return Collections.unmodifiableMap(routes);
    }

}
