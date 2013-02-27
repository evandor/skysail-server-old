package de.twenty11.skysail.server.management.internal;

import java.util.HashMap;
import java.util.Map;

import de.twenty11.skysail.server.management.ManagementRootResource;
import de.twenty11.skysail.server.management.RestartResource;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

public class ManagementUrlMapper implements UrlMapper {

    public static final String APP_PREFIX = "/" + ManagementApplicationDescriptor.APPLICATION_NAME;

    @Override
    public Map<String, String> provideUrlMapping() {
        Map<String, String> routes = new HashMap<String, String>();

        routes.put(APP_PREFIX, ManagementRootResource.class.getName());
        routes.put(APP_PREFIX + "/", ManagementRootResource.class.getName());

        Map<ApplicationDescriptor, String> graphPaths = ManagementApplication.get().getRelevantAppsAndPaths();
        for (ApplicationDescriptor application : graphPaths.keySet()) {
            String rootName = "tobedone";
            routes.put("/" + rootName + "/management/restart", RestartResource.class.getName());
        }
        return routes;
    }

}
