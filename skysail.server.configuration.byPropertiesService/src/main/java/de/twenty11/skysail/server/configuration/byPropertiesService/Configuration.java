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
package de.twenty11.skysail.server.configuration.byPropertiesService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import de.twenty11.skysail.server.services.BundleLoaderService;
import de.twenty11.skysail.server.services.ConfigService;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author carsten
 * 
 */
public class Configuration implements ConfigService {
    
    private static final String DEFAULT_CONF_LOCATION = "./etc/de.twentyeleven.skysail.cfg";

    /**slf4j based logging implementation. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** this configuration bundle is based on a single properties object. */
    private Properties props = new Properties();

    public Configuration() throws FileNotFoundException, IOException {
        String confLocation = Configuration.DEFAULT_CONF_LOCATION;
        // 1. try system property // TODO constants
        String locationFromSystemProperty = System.getProperty("skysail.confDir"); 
        logger.info("checking system property 'skysail.confDir', found value {}", locationFromSystemProperty);
        if (locationFromSystemProperty != null) {
            confLocation = locationFromSystemProperty + "/skysail.properties";
        }
        File propFile = new File(confLocation);
        logger.info("trying to load the configuration from file '{}'", propFile.getAbsolutePath());
        props.load(new FileReader(propFile));
    }

    @Override
    public String getString(String identifier) {
        return props.getProperty(identifier, null);
    }
    
    @Override
    public String getString(String identifier, String defaultValue) {
        return props.getProperty(identifier, defaultValue);
    }

    @Override
    public Properties getProperties(String identifier) {
        Properties result = new Properties();
        for (final Object key : props.keySet()) {
            if (key instanceof String) {
                String keyString = (String) key;
                if (keyString.startsWith(identifier)) {
                    String newKey = keyString.substring(identifier.length());
                    result.put(newKey, props.get(key));
                }
            }
        }
        return result;
    }

    public void getBundleLoaderService(BundleLoaderService service) {
        System.out.println("go you");
    }
    
    protected void activate(ComponentContext context)
    {
        System.out.println("active");
    }
    
    protected void deactivate(ComponentContext context)
    {
        System.out.println("done");
    }

}
