package de.twenty11.skysail.server;

import java.io.IOException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.internal.Blocker;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * Concurrency note from parent class: instances of this class or its subclasses can be invoked by
 * several threads at the same time and therefore must be thread-safe. You should be especially careful 
 * when storing state in member variables.
 *
 * @author carsten
 * 
 */
public class RestletOsgiApplication extends Application {

    /** slf4j based logger implementation */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** the restlet router. */
    protected volatile Router router;

    /** the osgi bundle context. */
    private static BundleContext bundleContext;

    public static void setBundleContext(BundleContext bundleContext) {
        RestletOsgiApplication.bundleContext = bundleContext;
    }
    
    /**
     * @param context
     *            OSGi bundleContext
     */
    // TODO dont know how to pass the context in the constructor when I am using
    // web.xml approach + jetty
//    public RestletOsgiApplication(final BundleContext context) {
//        RestletOsgiApplication.bundleContext = context;
//    }

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

    // TODO make abstract
    protected void attach() {
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

    public void attachToRouter(String key, Class<?> executor) {
        router.attach(key, executor);
        
    }

    public void detachFromRouter(Class<?> executor) {
        router.detach(executor);
        
    }

    public RouteList getRoutes() {
        return router.getRoutes();
    }

}