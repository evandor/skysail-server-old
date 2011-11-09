package de.twenty11.skysail.server.osgi.internal.put;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.internal.ServletRegistration;

/**
 * register Osgi Post Servlet
 * 
 * @author  carsten
 */
public class OsgiPutRegistration extends ServletRegistration {

	private static Logger logger = LoggerFactory
			.getLogger(OsgiPutRegistration.class);

	public void activate(ComponentContext context) {
		logger.info("activating OsgiPutRegistration");
		HttpServlet servlet = new OsgiPutServlet(lookup);
		try {
			servletAlias = "/put";
			http.registerServlet(servletAlias, servlet, null, null);
		} catch (Exception e) {
			logger.error("problem with registering servlet",e);
		}
	}


}
