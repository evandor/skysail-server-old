package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.UrlMapper;
import de.twenty11.skysail.server.osgi.bundles.BundleResource;
import de.twenty11.skysail.server.osgi.bundles.BundlesResource;

public class BundlesUrlMapper implements UrlMapper {

    public static final String RESTLET_BUNDLE_CONTEXT_ID = "bundles";
    
    public static final String RESTLET_BUNDLE_NAME_ID = "bundleName";

    @Override
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/", BundlesResource.class.getName());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/{" + RESTLET_BUNDLE_NAME_ID + "}/", BundleResource.class.getName());
        return queue;
    }

   

}
