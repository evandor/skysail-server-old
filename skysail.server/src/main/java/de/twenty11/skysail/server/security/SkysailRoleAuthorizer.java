package de.twenty11.skysail.server.security;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;
import org.restlet.security.Role;

public class SkysailRoleAuthorizer extends Authorizer {

    /** The modifiable list of authorized roles. */
    private final List<Role> authorizedRoles;

    public SkysailRoleAuthorizer(String roleName) {
        super(roleName);
        this.authorizedRoles = new CopyOnWriteArrayList<Role>();
    }

    @Override
    protected boolean authorize(Request request, Response response) {
        Subject currentUser = SecurityUtils.getSubject();
        if (getAuthorizedRoles().isEmpty()) {
            return true;
        } else {
            return currentUser.hasRole(getIdentifier());
        }
    }

    public List<Role> getAuthorizedRoles() {
        return authorizedRoles;
    }

}
