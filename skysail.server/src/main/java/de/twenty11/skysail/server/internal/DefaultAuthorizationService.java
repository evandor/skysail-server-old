package de.twenty11.skysail.server.internal;

import org.restlet.security.Authorizer;

import de.twenty11.skysail.server.security.AuthorizationService;
import de.twenty11.skysail.server.security.SkysailRoleAuthorizer;

public class DefaultAuthorizationService implements AuthorizationService {

    @Override
    public Authorizer getRoleAuthorizer(String securedByRole) {
        return new SkysailRoleAuthorizer(securedByRole);
    }

}
