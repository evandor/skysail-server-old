package de.twenty11.skysail.server.osgi.internal.delete;

import javax.servlet.http.HttpServlet;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.osgi.internal.ServletRegistration;

/**
 * @author  carsten
 */
public class OsgiDeleteRegistration extends ServletRegistration {

	private static Logger logger = LoggerFactory
			.getLogger(OsgiDeleteServlet.class);

	public void activate(ComponentContext context) {
		logger.info("activating OsgiDeleteServlet");
		HttpServlet servlet = new OsgiDeleteServlet(lookup);
		try {
			servletAlias = "/delete";
			http.registerServlet(servletAlias, servlet, null, null);
		} catch (Exception e) {
			logger.error("problem with registering servlet",e);
		}
	}


}
