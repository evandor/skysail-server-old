package de.twenty11.skysail.server.restletosgi;

import java.io.IOException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.restlet.Restlet;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author carsten
 *
 */
public class RestletOsgiApplication extends WadlApplication {

    /** the restlet router. */
    protected Router router;
    
    /** the osgi bundle context. */
    private static BundleContext bundleContext;

    /**
     * @param context OSGi bundleContext
     */
    public RestletOsgiApplication(final BundleContext context) {
        RestletOsgiApplication.bundleContext = context;
    }

    @Override
    public Restlet createRoot() {
        // Create a router
        router = new Router(getContext());

        // Attach the resources to the router
        //router.attach("/components/{componentName}/{command}/", RestletOsgiServerResource.class);
        router.attach("/components/", RestletOsgiServerResource.class);
        router.attach("/", SkysailRootServerResource.class);

        // Return the root router
        return router;
    }

    /**
     * attaches a path/classname pair to the router.
     * 
     * @param path the url sub-path to be handled
     * @param carer the class to take care of those requests
     */
    protected final void attachToRouter(final String path, final Class<?> carer) {
        router.attach(path, carer);
    }
    
    /**
     * router getter.
     * @return the router instance
     * @see org.restlet.ext.wadl.WadlApplication#getRouter()
     */
    public final Router getRouter() {
        return router;
    }
    
    // TODO Duplication in communicationUtils
    protected static final Template getFtlTemplate (String templatePath) {
        //ServiceReference serviceRef = bundleContext.getServiceReference(Configuration.class.getName(),"dynamicConfiguration=true");
        ServiceReference serviceRef = bundleContext.getServiceReference(Configuration.class.getName());
        Configuration service = (Configuration)bundleContext.getService(serviceRef);
        try {
            return service.getTemplate(templatePath);
        } catch (IOException e) {
            throw new RuntimeException("Problem accessing template '" + templatePath + "'");
        }
    }

}