package de.twenty11.skysail.server.security;

import org.restlet.security.Authorizer;

public interface AuthorizationService {

    Authorizer getRoleAuthorizer(String securedByRole);

}
