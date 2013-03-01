package de.twentyeleven.skysail.server.restlet.internal;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.restlet.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.services.ApplicationProvider;

public class Configuration implements ApplicationProvider {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private MyApplication application;

    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        logger.info("Activating Configuration Component for Skysail Restlet Extension");
        application = new MyApplication(componentContext.getBundleContext());
    }

    protected void deactivate(ComponentContext componentContext) {
        logger.info("Deactivating Configuration Component for Skysail Restlet Extension");
        application = null;
    }

    @Override
    public Application getApplication() {
        return application;
    }

}
