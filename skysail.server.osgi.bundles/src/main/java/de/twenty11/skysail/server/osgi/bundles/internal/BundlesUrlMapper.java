package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.osgi.bundles.restlet.BundleResource;
import de.twenty11.skysail.server.osgi.bundles.restlet.BundlesResource;
import de.twenty11.skysail.server.osgi.bundles.restlet.PackagesResource;
import de.twenty11.skysail.server.osgi.bundles.restlet.ServicesResource;
import de.twenty11.skysail.server.services.UrlMapper;

/**
 * Maps urls to resource handlers.
 * 
 * @author carsten
 * 
 */
public class BundlesUrlMapper implements UrlMapper {

    /**
     * It is really easier reading this if I do _not_ use constants.
     * 
     * @see de.twenty11.skysail.server.services.UrlMapper#getUrlMapping()
     */
    @Override
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());

        // @formatter:off
        queue.put("/osgi/bundles/",                              BundlesResource.class.getName());
        queue.put("/osgi/bundles/{bundleId}/",                   BundleResource.class.getName());
//        queue.put("/bundles/{bundleId}/bundleHeader/",      BundleHeaderResource.class.getName());
//        queue.put("/bundles/{bundleId}/importedPackages/",  ImportedPackagesResource.class.getName());
//        queue.put("/bundles/{bundleId}/exportedPackages/",  ExportedPackagesResource.class.getName());
//        queue.put("/bundles/{bundleId}/registeredService/", RegisteredServicesResource.class.getName());
//        queue.put("/bundles/consumedServices/",             ConsumedServicesResource.class.getName());

        queue.put("/osgi/packages/",                             PackagesResource.class.getName());

        queue.put("/osgi/services/",                             ServicesResource.class.getName());
//        queue.put("/services/{serviceId}/",                 ServiceDetailsResource.class.getName());

        // This needs dependency on equinox.ds, should be put in a bundle of its
        // own and contribute
        // queue.put("/" + Constants.BUNDLES + "/"
        // + Constants.SCR + "/", ScrResource.class.getName());
        // @formatter:on
        return queue;
    }

}
