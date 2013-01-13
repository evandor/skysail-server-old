package de.twenty11.skysail.server.management.internal;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

/**
 * @author carsten
 * 
 */
public class ManagementApplication extends RestletOsgiApplication {

    private static ManagementApplication self;
    private BundleContext bundleContext;

    /**
     * @param bundleContext
     * @param staticPathTemplate
     */
    public ManagementApplication(BundleContext bundleContext, String staticPathTemplate) {
        super(ManagementApplicationDescriptor.APPLICATION_NAME, staticPathTemplate);
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
        if (FrameworkUtil.getBundle(RestletOsgiApplication.class) != null) {
            new UrlMappingServiceListener(this);
            //new SkysailApplicationServiceListener(this);
        }
    }

    public Map<ApplicationDescriptor, String> getRelevantAppsAndPaths() {

        Map<ApplicationDescriptor, String> result = new HashMap<ApplicationDescriptor, String>();
        try {
            ServiceReference[] allSkysailApps = bundleContext.getAllServiceReferences(
                    ApplicationDescriptor.class.getName(), null);
            if (allSkysailApps != null) {
                for (ServiceReference serviceReference : allSkysailApps) {
                    ApplicationDescriptor skysailApp = (ApplicationDescriptor) bundleContext
                            .getService(serviceReference);
                    String skysailAppName = skysailApp.getApplicationDescription().getName();
                    result.put(skysailApp, "/" + skysailAppName);
                }
            }
            return result;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("invalid syntax", e);
        }

    }


}
