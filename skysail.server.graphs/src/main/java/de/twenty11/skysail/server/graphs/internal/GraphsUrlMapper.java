package de.twenty11.skysail.server.graphs.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.server.graphs.GraphsResource;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

public class GraphsUrlMapper implements UrlMapper {

    @Override
    public Map<String, String> provideUrlMapping() {
        Map<String, String> routes = new HashMap<>();
        
        Map<ApplicationDescriptor, List<String>> graphPaths = GraphsSkysailApplication.get().getRelevantAppsAndPaths();
        for (ApplicationDescriptor application : graphPaths.keySet()) {
            String rootName = application.getApplicationDescription().getName();
            for (String path : graphPaths.get(application)) {
                routes.put(path + "/graph", GraphsResource.class.getName());
            }
        }
        return routes;
    }

}
