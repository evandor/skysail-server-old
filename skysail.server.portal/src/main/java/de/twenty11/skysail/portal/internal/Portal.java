package de.twenty11.skysail.portal.internal;

import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;

public class Portal extends RestletOsgiApplication {

	public Portal(String staticPathTemplate) {
		super(staticPathTemplate);
	}

	@Override
	protected void attach() {
        new UrlMappingServiceListener(this);
	}

}
