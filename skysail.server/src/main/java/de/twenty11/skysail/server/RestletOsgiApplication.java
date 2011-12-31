package de.twenty11.skysail.server;

import java.io.IOException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.ext.wadl.WadlApplication;
import org.restlet.routing.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.internal.Blocker;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author carsten
 * 
 */
public class RestletOsgiApplication extends WadlApplication {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /** the restlet router. */
    protected Router router;

    /** the osgi bundle context. */
    private static BundleContext bundleContext;

    /**
     * @param context
     *            OSGi bundleContext
     */
    public RestletOsgiApplication(final BundleContext context) {
        RestletOsgiApplication.bundleContext = context;
    }

    @Override
    public final Restlet createRoot() {

        logger.info("creating new Router in {}", this.getClass().getName());
        router = new Router(getContext());
        
        // see http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);
        
        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/webapp/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        // TODO check ordering
        // add "this" classloader first (this is usually the "product" bundle
        customCL.addClassLoader(this.getClass().getClassLoader());
        // this is the "restletosgi" bundle
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        //customCL.addClassLoader(Router.class.getClassLoader());

        ClassLoaderDirectory directory = new ClassLoaderDirectory(getContext(),
               localReference,
               customCL);        

        router.attach("/static", directory);
        
        attach();

        Blocker blocker = new Blocker(getContext());
        Tracer tracer = new Tracer(getContext());
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        blocker.setNext(originalRequestFilter);
        originalRequestFilter.setNext(router);
        //tracer.setNext(router);
        return blocker;
    }

    protected void attach() {
        //router.attach("/components/", RestletOsgiServerResource.class);
        router.attach("/", SkysailRootServerResource.class);

    }

    /**
     * attaches a path/classname pair to the router.
     * 
     * @param path
     *            the url sub-path to be handled
     * @param carer
     *            the class to take care of those requests
     */
    protected final void attachToRouter(final String path, final Class<?> carer) {
        router.attach(path, carer);
    }

    /**
     * router getter.
     * 
     * @return the router instance
     * @see org.restlet.ext.wadl.WadlApplication#getRouter()
     */
    public final Router getRouter() {
        return router;
    }

    // TODO Duplication in communicationUtils
    protected static final Template getFtlTemplate(String templatePath) {
        // ServiceReference serviceRef =
        // bundleContext.getServiceReference(Configuration.class.getName(),"dynamicConfiguration=true");
        ServiceReference serviceRef = bundleContext.getServiceReference(Configuration.class.getName());
        Configuration service = (Configuration) bundleContext.getService(serviceRef);
        try {
            return service.getTemplate(templatePath);
        } catch (IOException e) {
            throw new RuntimeException("Problem accessing template '" + templatePath + "'");
        }
    }

}