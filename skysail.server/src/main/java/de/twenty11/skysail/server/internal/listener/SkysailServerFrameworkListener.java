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

package de.twenty11.skysail.server.internal.listener;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * listener for framework events.
 * 
 * No specific purposes apart from logging yet.
 * 
 * @author carsten
 *
 */
public class SkysailServerFrameworkListener implements FrameworkListener {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public final void frameworkEvent(final FrameworkEvent event) {
        logger.debug(event.toString());
    }

}
