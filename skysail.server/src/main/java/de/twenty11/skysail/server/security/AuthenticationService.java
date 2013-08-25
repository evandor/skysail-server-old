package de.twenty11.skysail.server.security;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

import de.twenty11.skysail.server.restlet.EnvironmentLoader;

public interface AuthenticationService {

    EnvironmentLoader getEnvironmentLoader();
    
    Authenticator getAuthenticator(Context context);

    void login(String username, String password, Request request, Response response);

    void logout();

}
