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

package de.twenty11.skysail.server.forms.internal;

import java.util.Collections;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.server.services.ApplicationDescriptor;

/**
 * The bundles activator.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** slf4j based logger implementation. */
    // private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SkysailApplicationServiceListener skysailApplicationServiceListener;

    /** {@inheritDoc} */
    public final void start(final BundleContext context) throws Exception {
        skysailApplicationServiceListener = new SkysailApplicationServiceListener(context);
    }

    /** {@inheritDoc} */
    public final void stop(final BundleContext context) throws Exception {
        skysailApplicationServiceListener = null;
    }

    public static Map<ApplicationDescriptor, FormsModel> getFormModels() {
        if (skysailApplicationServiceListener != null) {
            return skysailApplicationServiceListener.getFormModels();
        } else {
            return Collections.emptyMap();
        }
    }

}
