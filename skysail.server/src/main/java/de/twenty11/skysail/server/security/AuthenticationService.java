package de.twenty11.skysail.server.security;

public interface AuthenticationService {

    void login(String username, String password);
    
    void logout();

}
