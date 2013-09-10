/**
 * Copyright 2011 Carsten Gräf
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */
package de.twenty11.skysail.server.um.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Size;

import de.twenty11.skysail.common.forms.Field;

@Entity
@Table(name = "um_roles")
public class SkysailRole implements Serializable {

    private static final long serialVersionUID = -3115339834627271551L;

    @Id
    @TableGenerator(name = "UM_SKYSAIL_ROLE_TABLE_GEN", table = "SEQUENCE", pkColumnValue = "UM_SKYSAIL_ROLE_SEQ")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "UM_SKYSAIL_ROLE_TABLE_GEN")
    private int id;

    @Field
    @Size(min = 3, message = "name must have at least three characters")
    private String name;

    public SkysailRole() {
    }

    public SkysailRole(String roleName) {
        this.name = roleName;
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int roleId) {
        this.id = roleId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
