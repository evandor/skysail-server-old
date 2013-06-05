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

import java.util.Arrays;
import java.util.List;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.internal.LimitedQueue;

/**
 * listener for bundle events.
 * @author carsten
 *
 */
public class SkysailServerBundleListener implements BundleListener {

    private static final int QUEUE_SIZE = 100;

    private LimitedQueue<BundleEvent> lastEvents = new LimitedQueue<BundleEvent>(QUEUE_SIZE);

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Override
    public final void bundleChanged(final BundleEvent event) {
        lastEvents.add(event);
        logger.debug(event.toString());
    }

    public int getQueueSize() {
        return QUEUE_SIZE;
    }

    public List<BundleEvent> getNewestEvents() {
        return Arrays.asList(lastEvents.toArray(new BundleEvent[lastEvents.size()]));
    }

}
