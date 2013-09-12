package de.twenty11.skysail.server.domain;

import javax.validation.constraints.Size;

import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.common.forms.Form;

@Form(name="credentials")
public class Credentials {

    @Size(min = 3, message = "Username must have at least three characters")
    @Field
    private String username;
    
    @Size(min = 3, message = "Password must have at least three characters")
    @Field
    private String password;
    
    public Credentials() {
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
}
