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
package de.twenty11.skysail.server.osgi.internal.restlet;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.internal.ServletRegistration;
import de.twenty11.skysail.server.osgi.internal.get.OsgiGetServlet;

/**
 * register Osgi Post Servlet
 * 
 * @author  carsten
 */
public class OsgiRestletRegistration extends ServletRegistration {

	private static Logger logger = LoggerFactory
			.getLogger(OsgiRestletRegistration.class);

	public void activate(ComponentContext context) {
		logger.info("activating OsgiRestletRegistration");
		HttpServlet servlet = new OsgiRestletServlet(lookup);
		try {
			servletAlias = "/restlet";
			http.registerServlet(servletAlias, servlet, null, null);
		} catch (Exception e) {
			logger.error("problem with registering servlet",e);
		}
	}


}
