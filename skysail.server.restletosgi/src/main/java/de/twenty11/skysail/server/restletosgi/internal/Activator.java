package de.twenty11.skysail.server.restletosgi.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceListener;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.restlet.Component;
import org.restlet.data.Protocol;

import de.twenty11.skysail.server.restletosgi.RestletOsgiApplication;

/**
 * The bundles activator, taking care of creating a new restlet component,
 * application and url mapping listener.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** the port the restlet component will run at. */
    public static final int HTTP_SERVICE_PORT = 2011;

    /** the restlet component. */
    private Component       component;
    
    /** a service listener to take care of UrlMapper services. */
    private ServiceListener urlMappingListener;

    /**
     * bundles life cycle start method, creating a new restlet component,
     * a restlet application and the urlMapping listener.
     * 
     * @param context the osgi bundle context
     * @throws Exception starting the component may trigger an Exception
     * 
     */
    public final void start(final BundleContext context) throws Exception {
    	
//		ServiceTracker logServiceTracker = new ServiceTracker(context,
//				org.osgi.service.log.LogService.class.getName(), null);
//		logServiceTracker.open();
//		LogService logservice = (LogService) logServiceTracker.getService();
//
//		if (logservice != null)
//			logservice.log(LogService.LOG_INFO, "hey, I logged that!");
//
//        // Create a component
//        component = new Component();
//        component.getServers().add(Protocol.HTTP, Activator.HTTP_SERVICE_PORT);
//
//        // Create a restlet application
//        final RestletOsgiApplication application = new RestletOsgiApplication(context);
//
//        // Attach the application to the component
//        component.getDefaultHost().attachDefault(application);
//
//        // add missing protocols
//        //component.getClients().add(Protocol.CLAP);
//        
//        // and there you go...
//        component.start();
//
//        // ServiceListener for UrlMappings, to be unregistered when bundle is stopped
//        urlMappingListener = new UrlMappingServiceListener(context, application);
    }

    /**
     * stop lifecycle method, removing the urlMapping listener and stopping the
     * component.
     * 
     * @param context the bundleContext
     * @throws Exception stopping the component might trigger an exception
     * 
     */
    public final void stop(final BundleContext context) throws Exception {
//        context.removeServiceListener(urlMappingListener);
//        urlMappingListener = null;
//        component.stop();
    }

}
