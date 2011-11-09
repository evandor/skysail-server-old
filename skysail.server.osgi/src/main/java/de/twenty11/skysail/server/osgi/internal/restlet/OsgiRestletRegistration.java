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
