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

import org.restlet.Restlet;
import org.restlet.security.Role;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.SkysailRoleAuthorizer;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.MenuEntry;
import de.twenty11.skysail.server.services.MenuProvider;
import de.twenty11.skysail.server.um.repos.RoleRepository;
import de.twenty11.skysail.server.um.repos.UserRepository;
import de.twenty11.skysail.server.um.resources.AddRoleResource;
import de.twenty11.skysail.server.um.resources.AddUserResource;
import de.twenty11.skysail.server.um.resources.RolesResource;
import de.twenty11.skysail.server.um.resources.UserManagementRootResource;
import de.twenty11.skysail.server.um.resources.UserResource;
import de.twenty11.skysail.server.um.resources.UsersResource;

/**
 * 
 * @author graefca
 */
public class UserManagementApplication extends SkysailApplication implements ApplicationProvider, MenuProvider {

    private EntityManagerFactory enitityManagerFactory;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final Role adminRole;

    public UserManagementApplication() {
        setName("usermanagement");
        setDescription("Central User Configuration Application");
        adminRole = new Role("admin");
        getRoles().add(adminRole);
    }

    // @Override
    // public Restlet createInboundRoot() {
    // Restlet inboundRoot = super.createInboundRoot();
    // if (inboundRoot instanceof ChallengeAuthenticator) {
    // ChallengeAuthenticator auth = (ChallengeAuthenticator) inboundRoot;
    // Enroler enroler = new Enroler() {
    // @Override
    // public void enrole(ClientInfo clientInfo) {
    // List<Role> defaultRoles = new ArrayList<Role>();
    // defaultRoles.add(adminRole);
    // clientInfo.setRoles(defaultRoles);
    // }
    // };
    // auth.setEnroler(enroler);
    // }
    // return inboundRoot;
    // }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", UserManagementRootResource.class).setVisible(false));
        router.attach(new RouteBuilder("/", UserManagementRootResource.class).setVisible(false));
        router.attach(new RouteBuilder("/roles", RolesResource.class).setVisible(true).setText("Roles"));
        router.attach(new RouteBuilder("/roles/", AddRoleResource.class).setVisible(false));
        router.attach(new RouteBuilder("/users", roleSecured(UsersResource.class, "administrator")).setVisible(true)
                .setText("Users"));
        router.attach(new RouteBuilder("/users/", AddUserResource.class).setSecuredByRole(getRole("admin")).setVisible(
                false));
        router.attach(new RouteBuilder("/users/{username}", UserResource.class).setVisible(false));
    }

    private Restlet roleSecured(Class<UsersResource> resourceClass, String roleName) {
        // RoleAuthorizer authorizer = new RoleAuthorizer();
        SkysailRoleAuthorizer authorizer = new SkysailRoleAuthorizer(roleName);
        authorizer.getAuthorizedRoles().add(getRole(roleName));
        authorizer.setNext(resourceClass);
        return authorizer;
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

}