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

package de.twenty11.skysail.server.osgi.internal.post;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import de.twenty11.skysail.server.osgi.internal.OsgiHttpServlet;

/**
 * Servlet taking care of Post Requests
 * @author carsten
 */
public class OsgiPostServlet extends OsgiHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2252022158904372286L;

	private IComponentsLookup componentsLookup;

	private static Logger logger = LoggerFactory
			.getLogger(OsgiPostServlet.class);

	public OsgiPostServlet(IComponentsLookup lookup) {
		super();
		this.componentsLookup = lookup;
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		String pathInfo = request.getPathInfo();
        Principal userPrincipal = request.getUserPrincipal();
		Object data = dispatchPost(userPrincipal, pathInfo, request);
		serializeAndAddToRequest(request, pathInfo, data);
	}
	
	private Object dispatchPost(Principal userPrincipal, String pathInfo, HttpServletRequest request) {

		String componentName = getComponentFromPath(pathInfo);
		EntityService component = componentsLookup.getComponent(componentName);
		String serializedEntity = (String) request.getAttribute("entity");
		return null;//component.createEntity(userPrincipal,serializedEntity);
	}



}