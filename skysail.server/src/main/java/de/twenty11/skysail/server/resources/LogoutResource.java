package de.twenty11.skysail.server.resources;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class LogoutResource extends EntityServerResource<String> {

    public LogoutResource() {
        System.out.println("hiewr");
    }

    @Override
    protected void doInit() throws ResourceException {
    }

    @Override
    public String getData() {
        AuthenticationService authService = ((DefaultSkysailApplication) getApplication()).getAuthenticationService();
        // Authenticator authenticator = authService.getAuthenticator(getContext());
        authService.logout();
        return "";
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

    @Override
    public SkysailResponse<?> updateEntity(String entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMessage(String key) {
        // TODO Auto-generated method stub
        return null;
    }

}
