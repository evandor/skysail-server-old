package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.UrlMapper;
import de.twenty11.skysail.server.osgi.bundles.BundleHeaderResource;
import de.twenty11.skysail.server.osgi.bundles.BundleResource;
import de.twenty11.skysail.server.osgi.bundles.BundlesResource;
import de.twenty11.skysail.server.osgi.bundles.ConsumedServicesResource;
import de.twenty11.skysail.server.osgi.bundles.ExportedPackagesResource;
import de.twenty11.skysail.server.osgi.bundles.ImportedPackagesResource;
import de.twenty11.skysail.server.osgi.bundles.OsgiBundlesConstants;
import de.twenty11.skysail.server.osgi.bundles.PackagesResource;
import de.twenty11.skysail.server.osgi.bundles.RegisteredServicesResource;
import de.twenty11.skysail.server.osgi.bundles.ServicesResource;

public class BundlesUrlMapper implements UrlMapper {

    @Override
    public Map<String, String> getUrlMapping() {
        // TODO make nicer somehow
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        
        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/", BundlesResource.class.getName());
        
        queue.put("/" + OsgiBundlesConstants.PACKAGES + "/", PackagesResource.class.getName());

        // This needs dependency on equinox.ds, should be put in a bundle of its own and contribute
//        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/" 
//                        + OsgiBundlesConstants.SCR + "/", ScrResource.class.getName());

        queue.put("/" + OsgiBundlesConstants.SERVICES + "/", ServicesResource.class.getName());
        
//        queue.put("/" + OsgiBundlesConstants.SERVICES + "/{" + OsgiBundlesConstants.SERVICE_ID + "}/",
//                        ServiceDetailsResource.class.getName());

        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/{" + OsgiBundlesConstants.BUNDLE_ID + "}/",
                        BundleResource.class.getName());
        
        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/{" + OsgiBundlesConstants.BUNDLE_ID + "}/"
                        + OsgiBundlesConstants.BUNDLE_HEADER + "/", BundleHeaderResource.class.getName());
        
        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/{" + OsgiBundlesConstants.BUNDLE_ID + "}/"
                        + OsgiBundlesConstants.IMPORTED_PACKAGES + "/", ImportedPackagesResource.class.getName());
        
        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/{" + OsgiBundlesConstants.BUNDLE_ID + "}/"
                        + OsgiBundlesConstants.EXPORTED_PACKAGES + "/", ExportedPackagesResource.class.getName());
        
        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/{" + OsgiBundlesConstants.BUNDLE_ID + "}/"
                        + OsgiBundlesConstants.REGISTERD_SERVICES + "/", RegisteredServicesResource.class.getName());
        

        queue.put("/" + OsgiBundlesConstants.RESTLET_BUNDLE_CONTEXT_ID + "/"
                        + OsgiBundlesConstants.CONSUMED_SERVICES + "/", ConsumedServicesResource.class.getName());

        return queue;
    }

}
