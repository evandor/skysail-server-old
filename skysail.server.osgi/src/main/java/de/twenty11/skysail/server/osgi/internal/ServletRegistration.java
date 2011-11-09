package de.twenty11.skysail.server.osgi.internal;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;

import de.twenty11.skysail.server.osgi.IComponentsLookup;

public class ServletRegistration {

	protected HttpService http;
	protected String servletAlias;
	protected String imagesAlias;
	protected IComponentsLookup lookup;
	
	public void deactivate(ComponentContext context) {
		//logger.info("unregistering servlet");
		http.unregister(servletAlias);
		http.unregister(imagesAlias);
	}

	public void setHttp(HttpService value) {
		http = value;
	}

	public void unsetHttp() {
		http = null;
	}

	public void setLookup(IComponentsLookup value) {
		lookup = value;
	}


}
