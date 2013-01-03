package de.twenty11.skysail.server.management.internal;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Configuration implements ManagedService {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static ConfigurationAdmin configadmin;
    private ManagementApplication app;

    protected void activate(ComponentContext ctxt) {
        // logger.info("Activating Skysail Ext DbViewer Configuration Component");
        // dbViewerComponent = new ManagementComponent(ctxt.getBundleContext());
        app = new ManagementApplication(ctxt.getBundleContext(), "/static");
    }

    protected void deactivate(ComponentContext ctxt) {
        app = null;
    }

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        Configuration.configadmin = configadmin;
    }

    @Override
    public void updated(Dictionary properties) throws ConfigurationException {
        // TODO Auto-generated method stub

    }

}
