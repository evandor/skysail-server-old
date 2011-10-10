package de.skysail.server.security;

import java.security.Principal;

public class UserPrincipal implements Principal {

    private String username;

    public UserPrincipal(String name) {
        this.username = name;
    }
    
    @Override
    public String getName() {
        return username;
    }

}
