package de.twentyeleven.skysail.server.restlet.internal;

import org.osgi.framework.BundleContext;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twentyeleven.skysail.server.restlet.ApplicationsResource;
import de.twentyeleven.skysail.server.restlet.MyRootResource;

/**
 * @author carsten
 * 
 */
public class MyApplication extends SkysailApplication {

    public MyApplication(BundleContext bundleContext) {
        super(null);
        setDescription("RESTful OsgiMonitor bundle");
        setOwner("twentyeleven");
        setName("restlet");
        setAuthor("twenty11");
        setBundleContext(bundleContext);
    }
    
    protected void attach() {
        router.attach("", MyRootResource.class);
        router.attach("/", MyRootResource.class);
        router.attach("/applications", ApplicationsResource.class);
    }

}
