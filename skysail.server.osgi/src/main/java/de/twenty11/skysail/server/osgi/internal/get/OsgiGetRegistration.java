package de.twenty11.skysail.server.osgi.internal.get;

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
public class OsgiGetRegistration extends ServletRegistration {

	private static Logger logger = LoggerFactory
			.getLogger(OsgiGetRegistration.class);

	public void activate(ComponentContext context) {
		logger.info("activating OsgiPutRegistration");
		HttpServlet servlet = new OsgiGetServlet(lookup);
		try {
			servletAlias = "/get";
			http.registerServlet(servletAlias, servlet, null, null);
		} catch (Exception e) {
			logger.error("problem with registering servlet",e);
		}
	}


}
