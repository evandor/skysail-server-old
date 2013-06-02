package de.twenty11.skysail.server.management.internal;

import org.restlet.Context;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.management.MyRootResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;

/**
 * @author carsten
 * 
 */
public class MyApplication extends SkysailApplication {

    public MyApplication(Context componentContext) {
        super(componentContext == null ? null : componentContext.createChildContext());
        setDescription("RESTful Skysail server management bundle");
        setOwner("twentyeleven");
        setName("management");
    }

    protected void attach() {

        // @formatter:off
        router.attach(new RouteBuilder("", MyRootResource.class).setVisible(false));
        router.attach(new RouteBuilder("/serverLoad", ServerLoadResource.class).setVisible(true));
        //router.attach(new RouteBuilder("/upload", UploadResource.class).setVisible(true));
        // @formatter:on
    }
}
