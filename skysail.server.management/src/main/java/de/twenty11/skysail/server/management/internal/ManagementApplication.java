package de.twenty11.skysail.server.management.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.restlet.SkysailApplication;

/**
 * @author carsten
 * 
 */
public class ManagementApplication extends SkysailApplication {

    private static ManagementApplication self;
    private BundleContext bundleContext;

    /**
     * @param bundleContext
     * @param staticPathTemplate
     */
    public ManagementApplication(BundleContext bundleContext, String staticPathTemplate) {
        super(null);
        setDescription("RESTful skysail.server.management bundle");
        setOwner("twentyeleven");
        self = this;
        this.bundleContext = bundleContext;
    }

    /**
     * this is done to give osgi a chance to inject serives to restlet; should be changed to some javax.inject approach
     * (like using InjectedServerResource) once this is available.
     * 
     * @return
     */
    public static ManagementApplication get() {
        return self;
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
    }

    // TODO proper place for this here? what about multiple instances?
    protected void attach() {
        if (FrameworkUtil.getBundle(SkysailApplication.class) != null) {
            // new UrlMappingServiceListener(this);
            // new SkysailApplicationServiceListener(this);
        }
    }

}
