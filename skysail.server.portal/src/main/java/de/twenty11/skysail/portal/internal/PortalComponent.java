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

package de.twenty11.skysail.portal.internal;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concurrency note from parent class: instances of this class or its subclasses
 * can be invoked by several threads at the same time and therefore must be
 * thread-safe. You should be especially careful when storing state in member
 * variables.
 * 
 * @author carsten
 * 
 */
public class PortalComponent extends Component {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 
     */
    public PortalComponent() {
        getClients().add(Protocol.CLAP);

        // Create a restlet application
        logger.info("new restlet application: {}", Portal.class.getName());
        final Portal application = new Portal("/static");
        
        // Attach the application to the component and start it
        logger.info("attaching application and starting {}", this.toString());
        getDefaultHost().attachDefault(application);
    }

}
