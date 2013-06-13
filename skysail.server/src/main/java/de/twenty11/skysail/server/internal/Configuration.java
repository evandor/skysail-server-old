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

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.engine.Engine;
import org.restlet.security.MapVerifier;
import org.restlet.security.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.config.ServerConfiguration;
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.ComponentProvider;

public class Configuration implements ComponentProvider {

    public static final String CONTEXT_OPERATING_SYSTEM_BEAN = "de.twenty11.skysail.server.internal.Configuration.operatingSystemMxBean";

    private OperatingSystemMXBean operatingSystemMxBean = ManagementFactory.getOperatingSystemMXBean();

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

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);
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

        // Engine.setLogLevel(Level.ALL);
        Engine.setRestletLogLevel(Level.ALL);
        // System.setProperty("java.util.logging.config.file", "logging.config");
        logger.info("Starting component for Skysail...");
        String port = (String) serverConfig.getConfigForKey("port");
        logger.info("port was configured on {}", port);

        logger.info("");
        logger.info("====================================");
        logger.info("Starting skysail server on port {}", port);
        logger.info("====================================");
        logger.info("");

        restletComponent = new SkysailComponent(this.context);

        // Restlet defaultTargetClass = new DefaultResource(componentContext.getBundleContext());
        // restletComponent.getDefaultHost().attachDefault(defaultTargetClass);
        SkysailApplication defaultApplication = new DefaultSkysailApplication(componentContext.getBundleContext(),
                restletComponent.getContext());
        defaultApplication.setVerifier(verifier);
        defaultApplication.setServerConfiguration(serverConfig);
        restletComponent.getDefaultHost().attachDefault(defaultApplication);

        server = serverConfig.startStandaloneServer(port, restletComponent);

        serverActive = true;

        triggerAttachmentOfNewApplications();
    }

    private void triggerAttachmentOfNewApplications() {
        if (!serverActive) {
            return;
        }
        List<Application> newApplications = applications.getApplicationsInState(ApplicationState.NEW);
        for (Application application : newApplications) {
            try {
                applications.attach(application, restletComponent);
            } catch (Exception e) {
                logger.error("Problem with Application Lifecycle Management Defintion", e);
            }
        }
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

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        logger.info("setting configadmin in Skysail Configuration");
        this.configadmin = configadmin;
    }

    public synchronized void setServerConfiguration(de.twenty11.skysail.server.config.ServerConfiguration serverConfig) {
        logger.info("setting ServerConfiguration in Skysail Configuration");
        this.serverConfig = serverConfig;
    }

    public void addApplicationProvider(ApplicationProvider provider) {
        Validate.notNull(provider, "provider may not be null");

        logger.info("");
        logger.info("trying to add new application from {}", provider);
        Application application = provider.getApplication();
        if (application != null) {
            logger.info("==================================================");
            logger.info(" >>> found application '{}'", application.getName());
            logger.info("==================================================");
        } else {
            logger.warn("no application found, aborting...");
            return;
        }
        // TODO set verifier the same way?
        if (application.getContext() != null) {
            application.getContext().getAttributes().put(CONTEXT_OPERATING_SYSTEM_BEAN, operatingSystemMxBean);
        }
        if (application instanceof SkysailApplication) {
            logger.info("setting applications verifier from server configuration");
            ((SkysailApplication) application).setVerifier(verifier);
        }
        logger.info("attaching '{}' to defaultHost", "/" + application.getName());

        try {
            applications.add(application);
        } catch (Exception e) {
            logger.error("Problem with the Application Lifecycle Management", e);
        }

        triggerAttachmentOfNewApplications();
    }

    public void unsetApplicationProvider(ApplicationProvider provider) {
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

    @Override
    public Component getComponent() {
        return this.restletComponent;
    }

    @Override
    public Verifier getVerifier() {
        return this.verifier;
    }

}
