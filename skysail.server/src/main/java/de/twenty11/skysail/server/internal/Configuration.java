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

import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
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
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.ComponentProvider;

public class Configuration implements ComponentProvider {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);
    public static final String CONTEXT_OPERATING_SYSTEM_BEAN = "de.twenty11.skysail.server.internal.Configuration.operatingSystemMxBean";

    private SkysailComponent restletComponent;
    private Server server;
    private ComponentContext context;
    private ConfigurationAdmin configadmin;
    private ServerConfiguration serverConfig;
    private ServiceRegistration registration;
    private MapVerifier verifier;
    private ApplicationsHolder applications = new ApplicationsHolder();
    private boolean serverActive = false;

    protected synchronized void activate(ComponentContext componentContext) throws ConfigurationException {
        logger.info("Activating Skysail Component");
        this.context = componentContext;
        this.verifier = serverConfig.getVerifier(configadmin);

        Engine.setRestletLogLevel(Level.ALL);

        logger.info("Starting component for Skysail...");
        String port = (String) serverConfig.getConfigForKey("port");
        logger.info("port was configured on {}", port);

        logger.info("");
        logger.info("====================================");
        logger.info("Starting skysail server on port {}", port);
        logger.info("====================================");
        logger.info("");

        restletComponent = new SkysailComponent(this.context);

        SkysailApplication defaultApplication = new DefaultSkysailApplication(componentContext.getBundleContext(),
                restletComponent.getContext());
        defaultApplication.setVerifier(verifier);
        defaultApplication.setServerConfiguration(serverConfig);

        restletComponent.getDefaultHost().attachDefault(defaultApplication);

        server = serverConfig.startStandaloneServer(port, restletComponent);

        serverActive = true;

        triggerAttachmentOfNewApplications();
    }

    protected void deactivate(ComponentContext ctxt) {
        logger.info("Deactivating Skysail Ext Osgimonitor Configuration Component");
        serverActive = false;
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

    public void addApplicationProvider(ApplicationProvider provider) {
        Validate.notNull(provider, "provider may not be null");

        Application application = provider.getApplication();
        if (application == null) {
            logger.warn("provider {}'s application was null, ignoring...", provider.getClass().getName());
            return;
        }
        logger.info(" >>> new application '{}' registered <<<", application.getName());

        try {
            applications.add(application);
        } catch (Exception e) {
            logger.error("Problem with the Application Lifecycle Management", e);
        }

        triggerAttachmentOfNewApplications();
    }

    public void removeApplicationProvider(ApplicationProvider provider) {
        Application application = provider.getApplication();
        if (application != null) {
            logger.info("======================================================");
            logger.info(" >>> detaching application '{}'", application.getName());
            logger.info("======================================================");
            restletComponent.getDefaultHost().detach(application);
        } else {
            logger.warn("provider {}'s application was null", provider);
        }
    }

    private void triggerAttachmentOfNewApplications() {
        if (!serverActive) {
            return;
        }
        List<Application> newApplications = applications.getApplicationsInState(ApplicationState.NEW);
        for (Application application : newApplications) {
            try {
                applications.attach(application, restletComponent, verifier);
            } catch (Exception e) {
                logger.error("Problem with Application Lifecycle Management Defintion", e);
            }
        }
    }

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        logger.info("setting configadmin in Skysail Configuration");
        this.configadmin = configadmin;
    }

    public synchronized void setServerConfiguration(ServerConfiguration serverConfig) {
        logger.info("setting ServerConfiguration in Skysail Configuration");
        this.serverConfig = serverConfig;
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
