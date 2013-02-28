package de.twentyeleven.skysail.server.restlet.internal;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twentyeleven.skysail.server.restlet.ApplicationsResource;
import de.twentyeleven.skysail.server.restlet.MyRootResource;

/**
 * @author carsten
 * 
 */
public class MyApplication extends SkysailApplication {

    /**
     * @param staticPathTemplate
     * @param bundleContext
     */
    public MyApplication() {
        super();
        setDescription("RESTful OsgiMonitor bundle");
        setOwner("twentyeleven");
    }
    
    protected void attach() {
        router.attach("/", MyRootResource.class);
        router.attach("/applications", ApplicationsResource.class);
    }

}
