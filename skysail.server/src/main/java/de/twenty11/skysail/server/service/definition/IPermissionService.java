package de.twenty11.skysail.server.service.definition;

import java.security.Principal;

public interface IPermissionService {

    Role assertHasRole(Principal user, String rolename);        
    
    void assertAccessGranted(Principal user, Role role, String permission);

}
