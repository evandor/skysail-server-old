package de.twenty11.skysail.server.osgi.internal.post;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.internal.ServletRegistration;
import de.twenty11.skysail.server.osgi.internal.delete.OsgiDeleteServlet;

/**
 * register Osgi Post Servlet
 * 
 * @author  carsten
 */
public class OsgiPostRegistration extends ServletRegistration {

	private static Logger logger = LoggerFactory
			.getLogger(OsgiDeleteServlet.class);

	public void activate(ComponentContext context) {
		logger.info("activating OsgiPostRegistration");
		HttpServlet servlet = new OsgiPostServlet(lookup);
		try {
			servletAlias = "/post";
			http.registerServlet(servletAlias, servlet, null, null);
		} catch (Exception e) {
			logger.error("problem with registering servlet",e);
		}
	}


}
