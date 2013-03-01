/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.internal;

import java.util.logging.Level;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Server;
import org.restlet.engine.Engine;
import org.restlet.security.MapVerifier;
import org.restlet.security.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.config.ServerConfiguration;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.ComponentProvider;

public class Configuration implements ComponentProvider {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private SkysailComponent restletComponent;
    private Server server;
    private ComponentContext context;
    private ConfigurationAdmin configadmin;
    private ServerConfiguration serverConfig;
    private ServiceRegistration registration;
    private MapVerifier verifier;

    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        logger.info("Activating Skysail Component");
        this.context = componentContext;
        this.verifier = serverConfig.getVerifier(configadmin);

        // Engine.setLogLevel(Level.ALL);
        Engine.setRestletLogLevel(Level.ALL);
        // System.setProperty("java.util.logging.config.file", "logging.config");
        logger.info("Starting component for Skysail...");
        String port = (String) serverConfig.getConfigForKey("port");
        logger.info("port was configured on {}", port);

        logger.info("Starting standalone osgimonitor server on port {}", port);
        restletComponent = new SkysailComponent(this.context, verifier);

        server = serverConfig.startStandaloneServer(port, restletComponent);
    }

    protected void deactivate(ComponentContext ctxt) {
        logger.info("Deactivating Skysail Ext Osgimonitor Configuration Component");
        this.context = null;
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            logger.error("Exception when trying to stop standalone server", e);
        }
        if (registration != null) {
            registration.unregister();
        }
    }

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        logger.info("setting configadmin in OsgiMonitor Configuration");
        this.configadmin = configadmin;
    }

    public synchronized void setServerConfiguration(de.twenty11.skysail.server.config.ServerConfiguration serverConfig) {
        logger.info("setting configadmin in OsgiMonitor Configuration");
        this.serverConfig = serverConfig;
    }

    public void setApplicationProvider(ApplicationProvider provider) {
        logger.info("adding new application from {}", provider);
        Application application = provider.getApplication();
        if (application instanceof SkysailApplication) {
            ((SkysailApplication) application).setVerifier(verifier);
        }
        restletComponent.getDefaultHost().attach("/" + application.getName(), application);
    }

    public void unsetApplicationProvider(ApplicationProvider provider) {
        restletComponent.getDefaultHost().detach(provider.getApplication());
    }

    @Override
    public Component getComponent() {
        return this.restletComponent;
    }

    @Override
    public Verifier getVerifier() {
        return this.verifier;
    }

}
