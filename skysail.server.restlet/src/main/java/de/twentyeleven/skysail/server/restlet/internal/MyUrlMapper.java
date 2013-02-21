package de.twentyeleven.skysail.server.restlet.internal;

import java.util.HashMap;
import java.util.Map;

import de.twenty11.skysail.server.services.UrlMapper;
import de.twentyeleven.skysail.server.restlet.MyRootResource;

public class MyUrlMapper implements UrlMapper {

    public static final String APP_PREFIX = "/" + MyApplicationDescriptor.APPLICATION_NAME;

    @Override
    public Map<String, String> provideUrlMapping() {
        Map<String, String> queue = new HashMap<String, String>();

        queue.put(APP_PREFIX, MyRootResource.class.getName());
        queue.put(APP_PREFIX + "/", MyRootResource.class.getName());

        return queue;
    }

}
