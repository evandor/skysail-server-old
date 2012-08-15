package de.twenty11.skysail.server.config;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class Configuration implements ManagedService {

    private static String driverClassName = "default";

    @Override
    public synchronized void updated(Dictionary properties) throws ConfigurationException {
        if (properties != null) {
            try {
                driverClassName = (String) properties.get("skysail.db.driverClassName");
                // port = Integer.parseInt((String) properties.get("port"));
            } catch (Exception e) {
                // Ignore, use defaults
            }
        }
    }

    protected synchronized void deactivate() {
        // to be filled when necessary
    }
    
    public static String getDriverClassName() {
        return driverClassName;
    }

}
