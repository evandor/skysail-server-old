package de.twenty11.skysail.server.resources;

import java.util.Collections;
import java.util.List;

import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class LogoutResource extends ListServerResource2<String> {

    @Override
    protected List<String> getData() {
        AuthenticationService authService = ((DefaultSkysailApplication) getApplication()).getAuthenticationService();
        // authService.logout();
        return Collections.emptyList();
    }

    @Override
    public String getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(String entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
