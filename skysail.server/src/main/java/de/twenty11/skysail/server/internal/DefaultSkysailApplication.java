package de.twenty11.skysail.server.internal;

import org.osgi.framework.BundleContext;
import org.restlet.Context;

import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;

/**
 * TODO wird anscheinend nur in Json2BootstrapConverter & Configuration benutzt... ???
 *
 */
public class DefaultSkysailApplication extends SkysailApplication {

    public DefaultSkysailApplication(BundleContext bundleContext, Context componentContext) {
        super(componentContext.createChildContext(), bundleContext);
        setName("default");
    }

    @Override
    protected void attach() {
        router.attach("/", DefaultResource.class);
    }

}
