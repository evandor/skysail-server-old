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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author carsten
 *
 */
@Entity(name="SkysailRoles")
public class SkysailRole {

    @Id
    @GeneratedValue
    private int id;

    private String rolename;
    
    @OneToOne
    private SkysailClient client;

    public int getId() {
        return this.id;
    }
    
    public void setId(final int userId) {
        this.id = userId;
    }

    public String getRolename() {
        return this.rolename;
    }
    public void setRolename(final String name) {
        this.rolename = name;
    }
    
    public void setClient(SkysailClient client) {
        this.client = client;
    }
    
    public SkysailClient getClient() {
        return client;
    }
    
}
