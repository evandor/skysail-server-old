package de.twenty11.skysail.server.webapp.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    private WebappComponent webappComponent;

    @Override
    public void start(BundleContext context) throws Exception {
        webappComponent = new WebappComponent();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        webappComponent = null;
    }

}
