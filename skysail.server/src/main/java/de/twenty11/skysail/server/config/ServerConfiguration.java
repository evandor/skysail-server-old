package de.twenty11.skysail.server.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.restlet.security.MapVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.config.ConfigurationProvider;
import de.twenty11.skysail.server.Constants;

public class ServerConfiguration {// used to implements ManagedService,

    private final static Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);
    private static BasicDataSource defaultDS;

    private List<ConfigurationProvider> configurationProviders = Collections
            .synchronizedList(new ArrayList<ConfigurationProvider>());

    public ServerConfiguration() {
        System.out.println("Hier");
    }

    // @Override
    public synchronized void updated(Dictionary properties) throws ConfigurationException {
        if (properties != null) {
            // TODO set in memory defaults
            String newDriverClassName;
            String newUsername;
            String newPassword;
            String newDbUrl;
            try {
                newDriverClassName = (String) properties.get(Constants.SKYSAIL_DB_DRIVERCLASSNAME);
                newUsername = (String) properties.get(Constants.SKYSAIL_DB_USERNAME);
                newPassword = (String) properties.get(Constants.SKYSAIL_DB_PASSWORD);
                newDbUrl = (String) properties.get(Constants.SKYSAIL_DB_URL);
                // port = Integer.parseInt((String) properties.get(Constants.SKYSAIL_DB_USERNAME));
            } catch (Exception e) {
                // Ignore, use defaults
                logger.warn("could not set skysail database connection due to exception", e);
                return;
            }
            defaultDS = new BasicDataSource();
            defaultDS.setDriverClassName(newDriverClassName);
            defaultDS.setUsername(newUsername);
            defaultDS.setPassword(newPassword);
            defaultDS.setUrl(newDbUrl);
        }
    }

    public static BasicDataSource getDefaultDS() {
        return defaultDS;
    }

    public void addConfigurationProvider(ConfigurationProvider provider) {
        if (!(provider instanceof ServerConfiguration)) {
            configurationProviders.add(provider);
        }
    }

    public void removeConfigurationProvider(ConfigurationProvider provider) {
        configurationProviders.remove(provider);
    }

    // @Override
    public String getConfigForKey(String key) {
        for (ConfigurationProvider provider : configurationProviders) {
            if (provider.getConfigForKey(key) != null) {
                return provider.getConfigForKey(key);
            }
        }
        return null;
    }

    public boolean startComponent() {
        String componentToStart = (String) getConfigForKey("component");
        if (componentToStart == null || componentToStart.trim().length() == 0) {
            return true;
        }
        String[] packageParts = this.getClass().getName().split("\\.");
        for (String part : packageParts) {
            if (part.equals("de") || part.equals("server") || part.equals("ext") || part.equals("internal")) {
                continue;
            }
            if (part.equals(componentToStart)) {
                return true;
            }
        }
        return false;
    }

    public boolean setSecretVerifier(MapVerifier verifier, ConfigurationAdmin configadmin) throws IOException {
        org.osgi.service.cm.Configuration secrets;
        logger.info("gettings 'secrets' configuration...");
        if (configadmin == null) {
            logger.error("configadmin is not set, cannot proceed with configuration; no one will be able to log in!");
            return false;
        }
        secrets = configadmin.getConfiguration("secrets");
        if (secrets == null) {
            logger.error("could not find 'secrets' configuration; no one will be able to log in!");
            return false;
        }
        Dictionary secretsProperties = secrets.getProperties();
        if (secretsProperties == null || secretsProperties.keys() == null) {
            logger.error("secretProperties is null or empty; no one will be able to log in!");
            return false;
        }
        Enumeration keys = secretsProperties.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith("user.")) {
                String passCandidate = (String) secretsProperties.get(key);
                if (!passCandidate.startsWith("password.")) {
                    continue;
                }
                logger.info("setting password for user {}", key.substring("user.".length()));
                verifier.getLocalSecrets().put(key.substring("user.".length()),
                        passCandidate.substring("password.".length()).toCharArray());
            }
        }
        return true;
    }
}
