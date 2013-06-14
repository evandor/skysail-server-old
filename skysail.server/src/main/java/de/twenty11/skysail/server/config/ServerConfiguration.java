package de.twenty11.skysail.server.config;

import java.io.IOException;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.restlet.Server;
import org.restlet.security.MapVerifier;

import de.twenty11.skysail.common.config.ConfigurationProvider;

public interface ServerConfiguration {

    public abstract void addConfigurationProvider(ConfigurationProvider provider);

    public abstract void removeConfigurationProvider(ConfigurationProvider provider);

    public abstract String getConfigForKey(String key);

    public abstract boolean setSecretVerifier(MapVerifier verifier, ConfigurationAdmin configadmin) throws IOException;

    public abstract Server startStandaloneServer(String portAsString, org.restlet.Component restletComponent);

    public abstract MapVerifier getVerifier(ConfigurationAdmin configadmin) throws ConfigurationException;

}
