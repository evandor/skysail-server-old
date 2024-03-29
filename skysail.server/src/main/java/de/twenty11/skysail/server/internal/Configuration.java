/**
 * Copyright 2011 Carsten Gräf
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */
package de.twenty11.skysail.server.internal;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.osgi.framework.Bundle;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Server;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.security.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.config.ServerConfiguration;
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.core.osgi.internal.MenuState;
import de.twenty11.skysail.server.presentation.BootstrapHtmlConverter;
import de.twenty11.skysail.server.presentation.D3SimpleGraphConverter;
import de.twenty11.skysail.server.presentation.IFrame2BootstrapConverter;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.ComponentProvider;
import de.twenty11.skysail.server.services.MenuEntry;
import de.twenty11.skysail.server.services.MenuProvider;
import de.twenty11.skysail.server.services.MenuService;
import de.twenty11.skysail.server.utils.IOUtils;

public class Configuration implements ComponentProvider {

    private static Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static final String CONTEXT_OPERATING_SYSTEM_BEAN = "de.twenty11.skysail.server.internal.Configuration.operatingSystemMxBean";

    private SkysailComponent restletComponent;
    private Server server;
    private ComponentContext componentContext;
    private ConfigurationAdmin configadmin;
    private ServerConfiguration serverConfig;
    private final ApplicationsHolder applications = new ApplicationsHolder();
    private final MenusHolder menus;
    private boolean serverActive = false;
    private MenuService menuService;
    private AuthenticationService authService;
    private EventAdmin eventAdmin;

    public Configuration() throws Exception {
        menus = new MenusHolder(this);
    }

    protected synchronized void activate(ComponentContext componentContext) throws ConfigurationException {
        Engine.setRestletLogLevel(Level.ALL);

        logger.info("Activating Skysail Component");
        this.componentContext = componentContext;

        logger.info("Starting component for Skysail...");
        String port = serverConfig.getConfigForKey("port");
        logger.info("port was configured on {}", port);

        logger.info("");
        logger.info("====================================");
        logger.info("Starting skysail server on port {}", port);
        logger.info("====================================");
        logger.info("");

        restletComponent = new SkysailComponent();

        DefaultSkysailApplication defaultApplication = new DefaultSkysailApplication(componentContext);
        // defaultApplication.setVerifier(new Verifier() {
        // @Override
        // public int verify(Request request, Response response) {
        // return Verifier.RESULT_VALID;
        // }
        //
        // });
        defaultApplication.setServerConfiguration(serverConfig);
        defaultApplication.setAuthenticationService(authService);
        defaultApplication.setEventAdmin(eventAdmin);

        restletComponent.getDefaultHost().attachDefault(defaultApplication);

        server = serverConfig.startStandaloneServer(port, restletComponent);

        serverActive = true;

        List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
        // registeredConverters.add(new Json2HtmlConverter());
        registeredConverters.add(new BootstrapHtmlConverter());
        registeredConverters.add(new D3SimpleGraphConverter());
        registeredConverters.add(new IFrame2BootstrapConverter());
        // registeredConverters.add(new ToCsvConverter());

        updateDbConfig();

        triggerAttachmentOfNewApplications();
        triggerAttachmentOfNewMenus();
    }

    protected void deactivate(ComponentContext ctxt) {
        logger.info("Deactivating Skysail Ext Osgimonitor Configuration Component");

        triggerDetachmentOfMenus();
        // triggerDetachmentOfApplications();

        serverActive = false;
        this.componentContext = null;
        try {
            if (server != null) {
                server.stop();
            }
        } catch (Exception e) {
            logger.error("Exception when trying to stop standalone server", e);
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

    public void addMenuProvider(MenuProvider provider) {
        Validate.notNull(provider, "provider may not be null");
        logger.info(" >>> registering menu entries <<<");
        try {
            menus.add(provider.getMenuEntries());
        } catch (Exception e) {
            logger.error("Problem with the Menu Lifecycle Management", e);
        }
        triggerAttachmentOfNewMenus();
    }

    public void removeMenuProvider(MenuProvider provider) {
        List<MenuEntry> menuEntry = provider.getMenuEntries();
        if (menuEntry != null) {
            // restletComponent.getDefaultHost().detach(application);
        } else {
            logger.warn("provider {}'s menu was null", provider);
        }
    }

    private void triggerAttachmentOfNewApplications() {
        if (!serverActive) {
            return;
        }
        List<Application> newApplications = applications.getApplicationsInState(ApplicationState.NEW);
        for (Application application : newApplications) {
            try {
                Context appContext = restletComponent.getContext().createChildContext();
                application.setContext(appContext);
                if (application instanceof SkysailApplication) {
                    ((SkysailApplication) application).setEventAdmin(eventAdmin);
                }
                applications.attach(application, restletComponent, serverConfig, configadmin, authService);
            } catch (Exception e) {
                logger.error("Problem with Application Lifecycle Management Defintion", e);
            }
        }
    }

    private void bindEventAdmin(EventAdmin eventAdmin) {
        if (!serverActive) {
            return;
        }
        for (Application application : applications.getApplications()) {
            if (application instanceof SkysailApplication) {
                ((SkysailApplication) application).setEventAdmin(eventAdmin);
            }
        }
    }

    private void unbindEventAdmin() {
        if (!serverActive) {
            return;
        }
        for (Application application : applications.getApplications()) {
            if (application instanceof SkysailApplication) {
                ((SkysailApplication) application).unsetEventAdmin();
            }
        }
    }

    private void triggerAttachmentOfNewMenus() {
        if (!serverActive) {
            return;
        }
        List<MenuEntry> newMenus = menus.getMenusInState(MenuState.NEW);
        for (MenuEntry menu : newMenus) {
            try {
                menus.attach(menu, menuService);
            } catch (Exception e) {
                logger.error("Problem with Application Lifecycle Management Defintion", e);
            }
        }
    }

    private void triggerDetachmentOfMenus() {
        if (!serverActive) {
            return;
        }
        List<MenuEntry> attachedMenus = menus.getMenusInState(MenuState.ATTACHED);
        for (MenuEntry menu : attachedMenus) {
            try {
                menus.detach(menu, menuService);
            } catch (Exception e) {
                logger.error("Problem with Application Lifecycle Management Defintion", e);
            }
        }
    }

    public synchronized void setConfigAdmin(ConfigurationAdmin configadmin) {
        logger.info("setting configadmin in Skysail Configuration");
        this.configadmin = configadmin;
    }

    public synchronized void setEventAdmin(EventAdmin eventAdmin) {
        logger.info("setting eventAdmin in Skysail Configuration");
        this.eventAdmin = eventAdmin;
        bindEventAdmin(eventAdmin);
    }

    public synchronized void unsetEventAdmin(EventAdmin eventAdmin) {
        logger.info("unsetting eventAdmin in Skysail Configuration");
        this.eventAdmin = null;
        unbindEventAdmin();
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
    @Deprecated
    public Verifier getVerifier() {
        try {
            return serverConfig.getVerifier(configadmin);
        } catch (ConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void setMenuService(MenuService menuService) {
        this.menuService = menuService;
        triggerAttachmentOfNewMenus();
    }

    public boolean getServerActive() {
        return serverActive;
    }

    public MenuService getMenuService() {
        return menuService;
    }

    public void setAuthenticationService(AuthenticationService service) {
        this.authService = service;
    }

    private void updateSecuredUrls(Bundle bundle) {
        URL securityDef = bundle.getResource("META-INF/security.ini");
        if (securityDef != null) {
            try {
                // TODO do parsing with antlr
                BufferedInputStream inputStream = new BufferedInputStream(securityDef.openStream());
                String securityDefinitions = IOUtils.convertStreamToString(inputStream);
                Map<String, String> securityMapping = IOUtils.readSecurityDefinitions(securityDefinitions);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateDbConfig() {
        if (configadmin == null) {
            return;
        }
        try {
            for (String puName : scanBundlesForSkysailPUHeader()) {
                createConfigForDb(puName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private Set<String> scanBundlesForSkysailPUHeader() {
        Bundle[] bundles = componentContext.getBundleContext().getBundles();
        Set<String> result = new HashSet<String>();
        for (Bundle bundle : bundles) {
            Dictionary<String, String> headers = bundle.getHeaders();
            String xSkysailPU = headers.get(Constants.SKYSAIL_PERSISTENCE_UNIT);
            if (xSkysailPU != null) {
                result.add(xSkysailPU);
            }
        }
        return result;
    }

    private void createConfigForDb(String puName) throws Exception {
        // http://wiki.eclipse.org/Gemini/JPA/Documentation/OtherTopics#Configuration_Admin
        org.osgi.service.cm.Configuration config = configadmin.createFactoryConfiguration("gemini.jpa.punit", null);

        config.getProperties();

        // Create a dictionary and insert config properties (must include the punit name property)
        Dictionary props = new Hashtable();
        props.put("gemini.jpa.punit.name", puName);

        props.put("javax.persistence.jdbc.driver", serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_DRIVER));
        props.put("javax.persistence.jdbc.url", serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_URL));
        props.put("javax.persistence.jdbc.user", serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_USER));
        props.put("javax.persistence.jdbc.password", serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_PASSWORD));

        // props.put("eclipselink.ddl-generation", "create-tables");
        // props.put("eclipselink.ddl-generation.output-mode", "database");
        // props.put("eclipselink.session.customizer", "de.twenty11.skysail.server.um.init.db.Importer");
        // props.put("import.sql.file", "/initialImport.sql");

        // Causes config to be updated, or created if it did not already exist
        config.update(props);
    }
}
