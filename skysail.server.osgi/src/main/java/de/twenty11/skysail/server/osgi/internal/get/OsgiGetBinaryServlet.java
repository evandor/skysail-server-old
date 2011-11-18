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

package de.twenty11.skysail.server.osgi.internal.get;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import de.twenty11.skysail.server.osgi.internal.OsgiHttpServlet;

/**
 * Servlet taking care of Post Requests
 * 
 * @author carsten
 */
public class OsgiGetBinaryServlet extends OsgiHttpServlet {

	private IComponentsLookup componentsLookup;

	private static Logger logger = LoggerFactory
			.getLogger(OsgiGetBinaryServlet.class);

	public OsgiGetBinaryServlet(IComponentsLookup lookup) {
		super();
		this.componentsLookup = lookup;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws IOException {
		// dispatch based on pathInfo
		String pathInfo = request.getPathInfo();
		@SuppressWarnings("unchecked")
		Map<String,String[]> parameterMap = request.getParameterMap();
		Object data = dispatchGet(pathInfo, parameterMap);
		//serializeAndAddToRequest(request, pathInfo, data);
		request.setAttribute("payload", data);
	}

	private Object dispatchGet(String pathInfo, Map<String,String[]> parameterMap) {

		logger.info("OSGi Dispatcher Servlet called with '" + pathInfo + "'");
		// TODO simplify
		if (pathInfo.startsWith("/components/")) {
			// get component first
			String componentName = getComponentFromPath(pathInfo);
			EntityService component = componentsLookup
					.getComponent(componentName);
			String command = getCommandFromPath(pathInfo);

			if (command.equals("images")) {
				return component.getImage("notes.gif", "16x16");
			}
			// last resort
			return component.runCommand(command, null);
		}
		throw new NoSuchMethodError("could not match pathInfo '" + pathInfo
				+ "'");
	}

}