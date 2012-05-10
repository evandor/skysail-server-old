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

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;

public class SkysailBundleListener implements BundleListener {

    private static Logger logger;

    public SkysailBundleListener(final Logger myLogger) {
        SkysailBundleListener.logger = myLogger;
    }
    
    @Override
    public void bundleChanged(BundleEvent event) {
        String typeMessage = null;
        switch (event.getType()) {
        case BundleEvent.INSTALLED:
            typeMessage = "installed";
            break;
        case BundleEvent.LAZY_ACTIVATION:
            typeMessage = "lazy activation";
            break;
        case BundleEvent.RESOLVED:
            typeMessage = "resolved";
            break;
        case BundleEvent.STARTED:
            typeMessage = "started";
            break;
        case BundleEvent.STARTING:
            typeMessage = "starting";
            break;
        case BundleEvent.STOPPED:
            typeMessage = "stopped";
            break;
        case BundleEvent.STOPPING:
            typeMessage = "stopping";
            break;
        case BundleEvent.UNINSTALLED:
            typeMessage = "uninstalled";
            break;
        case BundleEvent.UNRESOLVED:
            typeMessage = "unresolved";
            break;
        case BundleEvent.UPDATED:
            typeMessage = "updated";
            break;
        default:
            typeMessage = "unknown";
            break;
        }
        logger.debug("BundleEvent '{}' received for bundle {}", typeMessage, event.getBundle());
    }
}