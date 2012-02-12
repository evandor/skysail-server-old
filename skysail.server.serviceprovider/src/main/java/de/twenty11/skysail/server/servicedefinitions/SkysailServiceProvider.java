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
package de.twenty11.skysail.server.servicedefinitions;



/**
 *
 * 
 * @author carsten
 *
 */
public class SkysailServiceProvider {

    
    private static ConfigService configService;
    
    //protected abstract void activate(ComponentContext ctxt);

    //protected abstract void deactivate(ComponentContext ctxt);

    public void setConfigService(ConfigService configService) {
        SkysailServiceProvider.configService = configService;
    }
    
    public void unsetConfigService(ConfigService configService) {
        SkysailServiceProvider.configService = null;
    }
    
    public static ConfigService getConfigService() {
        return configService;
    }
    
}
