package de.twenty11.skysail.server.resources;

import java.util.Collections;
import java.util.List;

import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.DefaultResource.AvailableApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class LogoutResource extends ListServerResource2<AvailableApplication> {

    @Override
    protected List<AvailableApplication> getData() {
        AuthenticationService authService = ((DefaultSkysailApplication) getApplication()).getAuthenticationService();
        authService.logout();
        return Collections.emptyList();
    }

}
