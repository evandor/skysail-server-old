package de.twenty11.skysail.server.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.config.ConfigurationProvider;
import de.twenty11.skysail.server.Constants;

public class Configuration implements ConfigurationProvider {// used to implements ManagedService,

    private final static Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static BasicDataSource defaultDS;

    private List<ConfigurationProvider> configurationProviders = Collections
            .synchronizedList(new ArrayList<ConfigurationProvider>());

    public Configuration() {
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
        if (!(provider instanceof Configuration)) {
            configurationProviders.add(provider);
        }
    }

    public void removeConfigurationProvider(ConfigurationProvider provider) {
        configurationProviders.remove(provider);
    }

    @Override
    public String getConfigForKey(String key) {
        for (ConfigurationProvider provider : configurationProviders) {
            if (provider.getConfigForKey(key) != null) {
                return provider.getConfigForKey(key);
            }
        }
        return null;
    }

}
