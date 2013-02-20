package de.twenty11.skysail.server.listener;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Route;
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.UrlMapper;

/**
 * A service listener which takes care of UrlMapper related services. A
 * UrlMapper provides a Map between a path (like "/components") and a class
 * name. An instance of that class then is expected to "take care" of requests
 * to the path.
 * 
 * The UrlMapper needs the osgi bundle context and a RestletOsgiApplication
 * objects the router of which will be updated accordingly in case of matching
 * service events. The constructor of this ServiceListener makes sure to add
 * itself to the bundle context with the appropriate filter.
 * 
 * 
 * @author carsten
 * 
 */
public class UrlMappingServiceListener implements ServiceListener {

    /** the osgi bundle context provided in the constructor. */
    private final BundleContext bundleContext;

    /** the restlet application. */
    private final SkysailApplication application;

    /** slf4j based logging implementation. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Set<String> mappings = new TreeSet<String>();
    
    private boolean addBundleName;

    public UrlMappingServiceListener(final SkysailApplication restletApp) {
        this(FrameworkUtil.getBundle(SkysailApplication.class).getBundleContext(), restletApp, false);
    }
    
    /**
     * Constructor which needs the bundleContext and the RestletOsgiApplication.
     * 
     * When this Listener is instantiated, the bundleContext is queried for all
     * the already existing serviceReferences to the
     * de.twenty11.skysail.server.UrlMapper class. The mappings provided by
     * those classes are then added to the applications router. This is done to
     * make sure that all the mappings from running bundles are taken into
     * account even though the restletosgi bundle might have been started later.
     * 
     * @param context
     *            the osgi bundle context
     * @param restletApp
     *            the restletOsgiApplication
     */
    public UrlMappingServiceListener(final BundleContext context, final SkysailApplication restletApp) {
        this(context, restletApp, false);
    }

    /**
     * Constructor which needs the bundleContext and the RestletOsgiApplication.
     * 
     * When this Listener is instantiated, the bundleContext is queried for all
     * the already existing serviceReferences to the
     * de.twenty11.skysail.server.UrlMapper class. The mappings provided by
     * those classes are then added to the applications router. This is done to
     * make sure that all the mappings from running bundles are taken into
     * account even though the restletosgi bundle might have been started later.
     * 
     * @param context
     *            the osgi bundle context
     * @param restletApp
     *            the restletOsgiApplication
     */
    public UrlMappingServiceListener(final BundleContext context, final SkysailApplication restletApp,
                    boolean addBundleName) {
        logger.debug("instantiating new UrlMappingServiceListener");
        this.bundleContext = context;
        this.application = restletApp;
        this.addBundleName = addBundleName;
        
        try {
            ServiceReference[] allUrlMappers = context.getAllServiceReferences(UrlMapper.class.getName(), null);
            if (allUrlMappers != null) {
                for (ServiceReference serviceReference : allUrlMappers) {
                    addNewMapping(context, serviceReference);
                }
            }
            logger.debug("adding new UrlMappingServiceListener to bundleContext");
            context.addServiceListener(this, "(objectClass=" + UrlMapper.class.getName() + ")");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("invalid syntax", e);
        }
    }

    /**
     * This method adds, removes or modifies the URL mappings of the
     * applications router according to the service events fired.
     * 
     * @param event
     *            the serviceEvent
     */
    @Override
    public final void serviceChanged(final ServiceEvent event) {
        if (event.getType() == ServiceEvent.REGISTERED) {
            addNewMapping(bundleContext, event.getServiceReference());
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            removeMapping(event.getServiceReference());
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            removeMapping(event.getServiceReference());
            addNewMapping(bundleContext, event.getServiceReference());
        }
    }

    /**
     * All the provided URL mappings (from the serviceReference implementing the
     * UrlMapper interface) are attached to the applications router.
     * 
     * Possible exceptions are translated into runtime exceptions.
     * 
     * @param context
     *            the bundleContext provided to the constructor
     * @param serviceReference
     *            the service reference to a UrlMapper.
     */
    private void addNewMapping(final BundleContext context, final ServiceReference serviceReference) {
        UrlMapper urlMapper = (UrlMapper) context.getService(serviceReference);
        Map<String, String> providedMapping = urlMapper.provideUrlMapping();
        if (providedMapping == null) {
            return;
        }
        String pathPrefix = "";
        if (addBundleName) {
            pathPrefix = "/" + serviceReference.getBundle().getSymbolicName();
        }
        for (Map.Entry<String, String> mapping : providedMapping.entrySet()) {
            try {
                logger.debug("adding new mapping from '{}' to '{}'", mapping.getKey(), mapping.getValue());
                @SuppressWarnings("unchecked")
				Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class.forName(mapping.getValue());
                application.attachToRouter(pathPrefix + mapping.getKey(), resourceClass);
                mappings.add(pathPrefix + mapping.getKey());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("was not able to find class " + mapping.getValue(), e);
            }
        }
        logCurrentMapping(application.getRoutes());
    }

    /**
     * All the provided URL mappings (from the serviceReference implementing the
     * UrlMapper interface) are removed from the applications router.
     * 
     * Possible exceptions are translated into runtime exceptions.
     * 
     * @param serviceRef
     *            the service reference to a UrlMapper.
     */
    private void removeMapping(final ServiceReference serviceRef) {
        UrlMapper urlMapper = (UrlMapper) bundleContext.getService(serviceRef);
        Map<String, String> providedMapping = urlMapper.provideUrlMapping();
        if (providedMapping == null) {
            return;
        }
        for (Map.Entry<String, String> mapping : providedMapping.entrySet()) {
            try {
                logger.debug("removing mapping '{}'", mapping);
                application.detachFromRouter(Class.forName(mapping.getValue()));
                mappings.remove(mapping.getKey());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("was not able to find class " + mapping.getValue(), e);
            }
        }
        logCurrentMapping(application.getRoutes());
    }

    /**
     * logs the current contents of the url mapping.
     * 
     * @param routes
     *            the url mapping
     */
    private void logCurrentMapping(final RouteList routes) {
        logger.info("current mapping is:");
        for (Route route : routes) {
            logger.info(route.toString());
        }
    }
    
    public Set<String> getMappings() {
        return mappings;
    }

}
