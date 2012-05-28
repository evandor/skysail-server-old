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

package de.twenty11.skysail.server.bundleloader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The bundles activator, taking care of creating a new restlet component,
 * application and url mapping listener.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        // Bundle installBundle = context
        // .installBundle("http://nexus.twentyeleven.de/content/groups/public/ch/qos/logback/logback-core/0.9.29/logback-core-0.9.29.jar");
        // System.out.println("installed" + installBundle.getSymbolicName());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        // TODO Auto-generated method stub

    }

}
