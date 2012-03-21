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
package de.twenty11.skysail.server.serviceprovider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.servicedefinitions.ConfigService;



/**
 *
 * 
 * @author carsten
 *
 */
public abstract class SkysailServiceProvider {

    /** slf4j based logging implementation. */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /** the skysail config service. */
    private ConfigService configService;
    
    
    /**
     * To be implemented according to their specific needs by subclasses.
     * @param ctxt provided by framework
     */
    // removing to avoid dependency on org.osgi
    //protected abstract void activate(ComponentContext ctxt);

    /**
     * To be implemented according to their specific needs by subclasses.
     * @param ctxt provided by framework
     */
    // removing to avoid dependency on org.osgi
    //protected abstract void deactivate(ComponentContext ctxt);

    public void setConfigService(final ConfigService service) {
        logger.info("skysail service provider: ConfigService set to {}", service);
        configService = service;
    }
    
    /**
     * called by the framework when the config service becomes unavailable.
     * @param service 
     */
    public void unsetConfigService(final ConfigService service) {
        logger.info("skysail service provider: ConfigService unset");
        configService = null;
    }
    
    /**
     * provides access to the skysail config service.
     * @return the skysail config service
     */
    public ConfigService getConfigService() {
        return configService;
    }
    
}
