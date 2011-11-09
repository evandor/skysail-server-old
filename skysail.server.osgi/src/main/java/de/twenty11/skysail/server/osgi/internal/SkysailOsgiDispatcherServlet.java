package de.twenty11.skysail.server.osgi.internal;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.IComponentsLookup;

/**
 * @author carsten
 */
public class SkysailOsgiDispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = -6300034012457029190L;
	private IComponentsLookup componentsLookup;

	private static Logger logger = LoggerFactory
			.getLogger(SkysailOsgiDispatcherServlet.class);

	public SkysailOsgiDispatcherServlet(IComponentsLookup lookup) {
		super();
		this.componentsLookup = lookup;
	}

	/** ===================================================================== */
	


}