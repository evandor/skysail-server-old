package de.twenty11.skysail.server.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.security.MapVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.config.ConfigurationProvider;

/**
 * keeps a list of {@link ConfigurationProvider}s to query for configuration keys.
 * 
 * ConfigurationProviders can be added or removed; the first provider with a not-null value for a given key is used.
 * There is no specific order enforced on the ConfigurationProviders.
 * 
 */
public class ServerConfiguration {

    private static Logger logger = LoggerFactory.getLogger(ServerConfiguration.class);

    private List<ConfigurationProvider> configurationProviders = Collections
            .synchronizedList(new ArrayList<ConfigurationProvider>());

    public void addConfigurationProvider(ConfigurationProvider provider) {
        logger.info("adding configuration provider {}", provider.getName());
        configurationProviders.add(provider);
    }

    public void removeConfigurationProvider(ConfigurationProvider provider) {
        logger.info("removing configuration provider {}", provider.getName());
        configurationProviders.remove(provider);
    }

    public String getConfigForKey(String key) {
        for (ConfigurationProvider provider : configurationProviders) {
            if (provider.getConfigForKey(key) != null) {
                return provider.getConfigForKey(key);
            }
        }
        return null;
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
        @SuppressWarnings("rawtypes")
        Dictionary secretsProperties = secrets.getProperties();
        if (secretsProperties == null || secretsProperties.keys() == null) {
            logger.error("secretProperties is null or empty; no one will be able to log in!");
            return false;
        }
        @SuppressWarnings("rawtypes")
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

    public Server startStandaloneServer(String portAsString, org.restlet.Component restletComponent) {
        try {
            Server server = new Server(Protocol.HTTP, Integer.valueOf(portAsString), restletComponent);
            server.start();
            return server;
        } catch (Exception e) {
            logger.error("Exception when starting standalone server trying to parse provided port (" + portAsString
                    + ")", e);
            return null;
        }
    }

    public MapVerifier getVerifier(ConfigurationAdmin configadmin) throws ConfigurationException {
        MapVerifier verifier = new MapVerifier();
        try {
            if (!setSecretVerifier(verifier, configadmin)) {
                logger.warn("not starting up the application due to encountered configuration problems.");
                throw new ConfigurationException("secrets", "encountered configuration problems");
            }
            return verifier;
        } catch (Exception e) {
            logger.error("Configuring secretVerifier encountered a problem: {}", e.getMessage());
            throw new ConfigurationException("secrets", "file not found", e);
        }
    }

}
