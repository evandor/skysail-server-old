//package de.twenty11.skysail.server.listener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.FrameworkUtil;
//import org.osgi.framework.InvalidSyntaxException;
//import org.osgi.framework.ServiceEvent;
//import org.osgi.framework.ServiceListener;
//import org.osgi.framework.ServiceReference;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.server.restlet.SkysailApplication;
//import de.twenty11.skysail.server.services.ApplicationDescriptor;
//
///**
// * A service listener which takes care of ApplicationDescriptor related services. 
// * 
// * 
// * 
// * @author carsten
// * 
// */
//public class SkysailApplicationServiceListener implements ServiceListener {
//
//    /**
//     * the osgi bundle context provided in the constructor.
//     */
//    private final BundleContext bundleContext;
//
//    /**
//     * slf4j based logging implementation.
//     */
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private List<ApplicationDescriptor> applicationDescriptors = new ArrayList<ApplicationDescriptor>();
//
//    public SkysailApplicationServiceListener(final SkysailApplication restletApp) {
//        this(FrameworkUtil.getBundle(SkysailApplication.class).getBundleContext(), restletApp, false);
//    }
//    
//    /**
//     * Constructor which needs the bundleContext and the RestletOsgiApplication.
//     * 
//     * When this Listener is instantiated, the bundleContext is queried for all
//     * the already existing serviceReferences to the
//     * de.twenty11.skysail.server.UrlMapper class. The mappings provided by
//     * those classes are then added to the applications router. This is done to
//     * make sure that all the mappings from running bundles are taken into
//     * account even though the restletosgi bundle might have been started later.
//     * 
//     * @param context
//     *            the osgi bundle context
//     * @param restletApp
//     *            the restletOsgiApplication
//     */
//    public SkysailApplicationServiceListener(final BundleContext context, final SkysailApplication restletApp) {
//        this(context, restletApp, false);
//    }
//
//    /**
//     * Constructor which needs the bundleContext and the RestletOsgiApplication.
//     * 
//     * 
//     * @param context
//     *            the osgi bundle context
//     * @param restletApp
//     *            the restletOsgiApplication
//     */
//    public SkysailApplicationServiceListener(final BundleContext context, final SkysailApplication restletApp,
//                    boolean addBundleName) {
//        logger.debug("instantiating new SkysailApplicationServiceListener");
//        this.bundleContext = context;
//        
//        try {
//            ServiceReference[] allUrlMappers = context.getAllServiceReferences(ApplicationDescriptor.class.getName(), null);
//            if (allUrlMappers != null) {
//                for (ServiceReference serviceReference : allUrlMappers) {
//                    addNewMapping(context, serviceReference);
//                }
//            }
//            logger.debug("adding new SkysailApplicationServiceListener to bundleContext");
//            context.addServiceListener(this, "(objectClass=" + ApplicationDescriptor.class.getName() + ")");
//        } catch (InvalidSyntaxException e) {
//            throw new RuntimeException("invalid syntax", e);
//        }
//    }
//
//    /**
//     * This method adds, removes or modifies the list of applicationDescriptors.
//     * 
//     * @param event
//     *            the serviceEvent
//     */
//    @Override
//    public final void serviceChanged(final ServiceEvent event) {
//        if (event.getType() == ServiceEvent.REGISTERED) {
//            addNewMapping(bundleContext, event.getServiceReference());
//        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
//            removeMapping(event.getServiceReference());
//        } else if (event.getType() == ServiceEvent.MODIFIED) {
//            removeMapping(event.getServiceReference());
//            addNewMapping(bundleContext, event.getServiceReference());
//        }
//    }
//
//    /**
//     * All the provided URL mappings (from the serviceReference implementing the
//     * UrlMapper interface) are attached to the applications router.
//     * 
//     * Possible exceptions are translated into runtime exceptions.
//     * 
//     * @param context
//     *            the bundleContext provided to the constructor
//     * @param serviceReference
//     *            the service reference to a UrlMapper.
//     */
//    private void addNewMapping(final BundleContext context, final ServiceReference serviceReference) {
//        ApplicationDescriptor urlMapper = (ApplicationDescriptor) context.getService(serviceReference);
//        applicationDescriptors.add(urlMapper);
//    }
//
//    /**
//     * All the provided URL mappings (from the serviceReference implementing the
//     * UrlMapper interface) are removed from the applications router.
//     * 
//     * Possible exceptions are translated into runtime exceptions.
//     * 
//     * @param serviceRef
//     *            the service reference to a UrlMapper.
//     */
//    private void removeMapping(final ServiceReference serviceRef) {
//        ApplicationDescriptor appService = (ApplicationDescriptor) bundleContext.getService(serviceRef);
//        applicationDescriptors.remove(appService);
//    }
//
// }
