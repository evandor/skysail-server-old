package de.twentyeleven.skysail.server.restlet.internal;

import org.osgi.framework.BundleContext;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twentyeleven.skysail.server.restlet.MyRootResource;

/**
 * @author carsten
 * 
 */
public class MyApplication extends SkysailApplication {

    // non-arg constructor needed for scr
    public MyApplication() {
        this("dummy", null);
    }

    /**
     * @param staticPathTemplate
     * @param bundleContext
     */
    public MyApplication(String staticPathTemplate, BundleContext bundleContext) {
        super(MyApplicationDescriptor.APPLICATION_NAME);
        setDescription("RESTful OsgiMonitor bundle");
        setOwner("twentyeleven");
        setBundleContext(bundleContext);
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
    }

    protected void attach() {
        router.attach("/myapplication", MyRootResource.class);
    }

}
