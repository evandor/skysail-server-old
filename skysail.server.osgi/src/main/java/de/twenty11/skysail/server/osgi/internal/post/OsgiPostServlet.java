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
		return component.createEntity(userPrincipal,serializedEntity);
	}



}