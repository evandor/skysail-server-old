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

import de.twenty11.skysail.server.service.definition.ConfigService;

public class Configuration implements ConfigService {

    Properties props = new Properties();
    
    public Configuration() throws FileNotFoundException, IOException {
        //new File("./whereami").createNewFile();
        props.load(new FileReader(new File("./conf/skysail.properties")));
    }
    
    @Override
    public String getString(String identifier) {
        return props.getProperty(identifier);
    }

}
