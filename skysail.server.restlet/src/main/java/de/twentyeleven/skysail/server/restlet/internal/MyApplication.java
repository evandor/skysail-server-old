package de.twentyeleven.skysail.server.restlet.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.restlet.SkysailApplication;

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
        super(MyApplicationDescriptor.APPLICATION_NAME, staticPathTemplate);
        setDescription("RESTful OsgiMonitor bundle");
        setOwner("twentyeleven");
        setBundleContext(bundleContext);
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
    }

    // TODO proper place for this here? what about multiple instances?
    protected void attach() {
        if (FrameworkUtil.getBundle(SkysailApplication.class) != null) {
            urlMappingServiceListener = new UrlMappingServiceListener(this);
            // new SkysailApplicationServiceListener(this);
        }
    }

}
