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

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import de.twenty11.skysail.server.osgi.internal.OsgiHttpServlet;

/**
 * Servlet taking care of "restlet" Requests
 * 
 * @author carsten
 */
public class OsgiRestletServlet extends OsgiHttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2252022158904372286L;

    private IComponentsLookup componentsLookup;

    private static Logger logger = LoggerFactory.getLogger(OsgiRestletServlet.class);

    public OsgiRestletServlet(IComponentsLookup lookup) {
        super();
        this.componentsLookup = lookup;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String pathInfo = request.getPathInfo();
        Principal userPrincipal = request.getUserPrincipal();
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = request.getParameterMap();
        Object data = dispatchGet(userPrincipal, pathInfo, parameterMap);
        request.setAttribute("payload", data);
    }

    /**
     * Dispatching based on provided pathInfo by determining the component and the command.
     * 
     * If needed, the returned business object is serialized, as the OSGi layer and the restlet
     * server have different class loaders which leads to class cast exceptions. 
     * @param userPrincipal 
     * 
     * @param pathInfo containing information about component and path
     * @param parameterMap additional information passed to executing method
     * @return
     */
    private Object dispatchGet(Principal userPrincipal, String pathInfo, Map<String, String[]> parameterMap) {

        logger.info("Dispatching '" + pathInfo + "'");

        String componentName = getComponentFromPath(pathInfo);
        EntityService component = componentsLookup.getComponent(componentName);
        String command = getCommandFromPath(pathInfo);

        if (!pathInfo.startsWith("/components/")) {
            throw new RuntimeException("pathInfo doesn't start with expected '/components/'");
        }
        
        if (pathInfo.equals("/components/")) {
            return serialize(componentsLookup.getComponents(userPrincipal));
        }
        // list of standard commands
        if (command.equals("menus")) {
            return serialize(component.getSubMenu(getQueryFromPath(pathInfo)));
        } else if (command.equals("show")) {
            return serialize(component.getGridData(userPrincipal, pathInfo, parameterMap));
        } else if (command.equals("fields")) {
            return serialize(component.getGridInfo(pathInfo, parameterMap));
        }
        // last resort for non-standard commands
        return component.runCommand(command, parameterMap);
    }

}