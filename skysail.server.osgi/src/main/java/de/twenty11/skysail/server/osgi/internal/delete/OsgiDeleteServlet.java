package de.twenty11.skysail.server.osgi.internal.delete;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import de.twenty11.skysail.server.osgi.internal.OsgiHttpServlet;

/**
 * @author carsten
 */
public class OsgiDeleteServlet extends OsgiHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2252022158904372286L;

	private IComponentsLookup componentsLookup;

	private static Logger logger = LoggerFactory.getLogger(OsgiDeleteServlet.class);

	public OsgiDeleteServlet(IComponentsLookup lookup) {
		super();
		logger.info ("Injecting IComponentsLookup into OsgiDeleteServlet");
		this.componentsLookup = lookup;
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) {
		String componentName = getComponentFromPath(req.getPathInfo());
		EntityService component = componentsLookup.getComponent(componentName);
		Long id = new Long(getEntityIdFromRequest(req.getPathInfo()));
		component.deleteEntity(id);
	}
	
	private int getEntityIdFromRequest(String requestURI) {
		String[] splits = requestURI.split("/");
		// TODO make safe(r)
		return Integer.parseInt(splits[splits.length - 1]);
	}


}