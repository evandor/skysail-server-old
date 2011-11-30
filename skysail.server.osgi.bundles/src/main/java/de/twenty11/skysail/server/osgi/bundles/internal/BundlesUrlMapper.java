package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.UrlMapper;
import de.twenty11.skysail.server.osgi.bundles.BundleResource;
import de.twenty11.skysail.server.osgi.bundles.BundlesResource;
import de.twenty11.skysail.server.osgi.bundles.ImportedPackagesResource;

public class BundlesUrlMapper implements UrlMapper {

    public static final String RESTLET_BUNDLE_CONTEXT_ID = "bundles";

    public static final String BUNDLE_ID = "bundleId";

    public static final String IMPORTED_PACKAGES = "importedPackages";

    @Override
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/", BundlesResource.class.getName());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/{" + BUNDLE_ID + "}/", BundleResource.class.getName());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/{" + BUNDLE_ID + "}/{" + IMPORTED_PACKAGES + "}/",
                ImportedPackagesResource.class.getName());
        return queue;
    }

}
