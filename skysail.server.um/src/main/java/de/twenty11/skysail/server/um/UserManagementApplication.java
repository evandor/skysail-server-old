/*
 * Copyright 2013 graefca.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.twenty11.skysail.server.um;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManagerFactory;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.MenuEntry;
import de.twenty11.skysail.server.services.MenuProvider;
import de.twenty11.skysail.server.um.domain.SkysailUser;
import de.twenty11.skysail.server.um.repos.RoleRepository;
import de.twenty11.skysail.server.um.repos.UserRepository;
import de.twenty11.skysail.server.um.resources.AddRoleResource;
import de.twenty11.skysail.server.um.resources.AddUserResource;
import de.twenty11.skysail.server.um.resources.CurrentUserResource;
import de.twenty11.skysail.server.um.resources.RolesResource;
import de.twenty11.skysail.server.um.resources.UserResource;
import de.twenty11.skysail.server.um.resources.UsersResource;
import de.twenty11.skysail.server.um.services.UserManager;

public class UserManagementApplication extends SkysailApplication implements ApplicationProvider, MenuProvider,
        UserManager {

    private EntityManagerFactory enitityManagerFactory;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UserManagementApplication() {
        setName("usermanagement");
        setDescription("Central User Configuration Application");
    }

    @Override
    protected void attach() {
        // router.attach(new RouteBuilder("", UserManagementRootResource.class).setVisible(false));
        // router.attach(new RouteBuilder("/", UserManagementRootResource.class).setVisible(false));
        router.attach(new RouteBuilder("/roles", RolesResource.class).setVisible(true).setText("Roles"));
        router.attach(new RouteBuilder("/roles/", AddRoleResource.class).setVisible(false));
        router.attach(new RouteBuilder("/currentUser", CurrentUserResource.class).setVisible(false));
        router.attach(new RouteBuilder("/users", UsersResource.class).setSecuredByRole("admin").setVisible(true)
                .setText("Users"));
        router.attach(new RouteBuilder("/users/", AddUserResource.class).setSecuredByRole("admin").setVisible(false));
        router.attach(new RouteBuilder("/users/{username}", UserResource.class).setVisible(false));
    }

    public synchronized UserRepository getUserRepository() {
        if (this.userRepository == null) {
            this.userRepository = new UserRepository(enitityManagerFactory);
        }
        return this.userRepository;
    }

    public synchronized RoleRepository getRoleRepository() {
        if (this.roleRepository == null) {
            this.roleRepository = new RoleRepository(enitityManagerFactory);
        }
        return this.roleRepository;
    }

    public synchronized void setEntityManager(EntityManagerFactory emf) {
        this.enitityManagerFactory = emf;
    }

    @Override
    public List<MenuEntry> getMenuEntries() {
        return Arrays.asList(new MenuEntry("main", "um", getName() + "/users"));
    }

    @Override
    public SkysailUser findByUsername(String username) {
        return getUserRepository().getByName(username);
    }

}
