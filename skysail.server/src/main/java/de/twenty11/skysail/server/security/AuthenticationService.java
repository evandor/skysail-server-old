package de.twenty11.skysail.server.security;

import org.restlet.Context;
import org.restlet.security.Authenticator;

public interface AuthenticationService {

    Authenticator getAuthenticator(Context context);

    void login(String username, String password);

    void logout();
}
