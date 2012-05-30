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

package de.twenty11.skysail.server.logging.osgi_over_slf4j;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailFrameworkListener implements FrameworkListener {

    // private static Logger logger;
    private final Logger logger = LoggerFactory.getLogger(SkysailBundleListener.class);

    // public SkysailFrameworkListener(Logger myLogger) {
    // SkysailFrameworkListener.logger = myLogger;
    // }

    @Override
    public void frameworkEvent(FrameworkEvent event) {
        switch (event.getType()) {
        case FrameworkEvent.ERROR:
            logger.error("Framework ERROR in bundle {}: {}", event.getBundle(), event.getThrowable().getMessage());
            break;
        case FrameworkEvent.INFO:
            logger.info("Framework ERROR in bundle {}", event.getBundle());
            break;
        case FrameworkEvent.PACKAGES_REFRESHED:
            logger.debug("Framework event PACKAGES_REFRESHED in bundle {}", event.getBundle());
            break;
        case FrameworkEvent.STARTED:
            logger.info("bundle {} STARTED", event.getBundle());
            break;
        case FrameworkEvent.STARTLEVEL_CHANGED:
            logger.info("Framework event STARTLEVEL_CHANGED for bundle {}", event.getBundle());
            break;
        case FrameworkEvent.STOPPED:
            logger.info("bundle {} STOPPED", event.getBundle());
            break;
        case FrameworkEvent.WARNING:
            logger.warn("Framework event WARNING in bundle {}", event.getBundle());
            break;
        default:
            logger.error("event {} not logged properly", event.getType());
            break;
        }

    }
}
