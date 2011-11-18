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

package de.twenty11.skysail.server.osgi.internal;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.IComponentsLookup;

/**
 * @author carsten
 */
public class SkysailOsgiDispatcher {

    private HttpService http;
    private String servletAlias;
    /**
 */
    private String imagesAlias;
    /**
	 */
    private IComponentsLookup lookup;

    private static Logger logger = LoggerFactory.getLogger(SkysailOsgiDispatcherServlet.class);

    public void activate(ComponentContext context) {
        logger.info("activating skysailOsgiDispatcherServlet");
        HttpServlet servlet = new SkysailOsgiDispatcherServlet(lookup);
        try {
            servletAlias = "/dispatcher";
            http.registerServlet(servletAlias, servlet, null, null);
        } catch (Exception e) {
            logger.error("problem with registering servlet", e);
        }
    }

    public void deactivate(ComponentContext context) {
        logger.info("unregistering servlet");
        http.unregister(servletAlias);
        http.unregister(imagesAlias);
    }

    /**
     * @param value
     */
    public void setHttp(HttpService value) {
        http = value;
    }

    public void unsetHttp() {
        http = null;
    }

    /**
     * @param value
     */
    public void setLookup(IComponentsLookup value) {
        lookup = value;
    }

}
