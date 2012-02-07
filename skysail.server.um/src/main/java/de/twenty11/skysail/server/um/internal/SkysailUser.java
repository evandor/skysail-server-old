/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.um.internal;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author carsten
 *
 */
@Entity(name="SkysailUsers")
public class SkysailUser {

    @Id
    @GeneratedValue
    private int id;

    /** hmmm.... = email address???. */
    private String login;
    
    /** users password. */
    private String password;
    
    private Set<SkysailRole> roles;

    private Set<SkysailClient> clients;

    private Set<SkysailPermission> permissions;

    public int getId() {
        return this.id;
    }
    
    public void setId(final int userId) {
        this.id = userId;
    }

    public String getLogin() {
        return this.login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    
    @OneToMany
    public Set<SkysailRole> getRoles() {  
        return roles;
    }
    
    public void setRoles(Set<SkysailRole> roles) {
        this.roles = roles;
    }
    
    public Set<SkysailClient> getClients() {
        return clients;
    }
    
    public void setClients(Set<SkysailClient> clients) {
        this.clients = clients;
    }
    
    public void setPermissions(Set<SkysailPermission> permissions) {
        this.permissions = permissions;
    }
    
    @OneToMany
    public Set<SkysailPermission> getPermissions() {
        return permissions;
    }
    
    public final void setPassword(final String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return password;
    }
}
