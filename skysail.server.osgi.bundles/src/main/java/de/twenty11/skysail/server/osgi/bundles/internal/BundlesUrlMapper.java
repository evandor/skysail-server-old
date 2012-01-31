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
import de.twenty11.skysail.server.osgi.bundles.PackagesResource;
import de.twenty11.skysail.server.osgi.bundles.RegisteredServicesResource;
import de.twenty11.skysail.server.osgi.bundles.ServiceDetailsResource;
import de.twenty11.skysail.server.osgi.bundles.ServicesResource;

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
     * @see de.twenty11.skysail.server.UrlMapper#getUrlMapping()
     */
    @Override
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());

        // @formatter:off
        queue.put("/bundles/",                              BundlesResource.class.getName());
        queue.put("/bundles/{bundleId}/",                   BundleResource.class.getName());
        queue.put("/bundles/{bundleId}/bundleHeader/",      BundleHeaderResource.class.getName());
        queue.put("/bundles/{bundleId}/importedPackages/",  ImportedPackagesResource.class.getName());
        queue.put("/bundles/{bundleId}/exportedPackages/",  ExportedPackagesResource.class.getName());
        queue.put("/bundles/{bundleId}/registeredService/", RegisteredServicesResource.class.getName());
        queue.put("/bundles/consumedServices/",             ConsumedServicesResource.class.getName());

        queue.put("/packages/",                             PackagesResource.class.getName());

        queue.put("/services/",                             ServicesResource.class.getName());
        queue.put("/services/{serviceId}/",                 ServiceDetailsResource.class.getName());

        // This needs dependency on equinox.ds, should be put in a bundle of its
        // own and contribute
        // queue.put("/" + Constants.BUNDLES + "/"
        // + Constants.SCR + "/", ScrResource.class.getName());
        // @formatter:on
        return queue;
    }

}
