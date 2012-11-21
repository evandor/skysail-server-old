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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailEntityManagerProvider {

    /** slf4j based logger. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private EntityManagerFactory emf;

    protected void activate(final ComponentContext component) {
        logger.info("activating component in {}", component.getBundleContext().getBundle().getSymbolicName());
        try {
            EntityManager em = emf.createEntityManager();
            validateDefaultRole(em);
            validateDefaultUser(em);
            em.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setEntityManager(EntityManagerFactory emf) {
        this.emf = emf;
        RoleService.setEmf(emf);
    }

    private void validateDefaultRole(EntityManager em) {
        em.getTransaction().begin();
        SkysailRole adminrole = new SkysailRole();
        adminrole.setRolename("administrator");
        adminrole.setId(1);
        em.persist(adminrole);
        em.getTransaction().commit();
    }

    private void validateDefaultUser(EntityManager em) {
        em.getTransaction().begin();
        SkysailRole role = new RoleService().getRole(1);
        Set<SkysailRole> roles = new HashSet<SkysailRole>();
        roles.add(role);

        SkysailUser admin = new SkysailUser();
        admin.setLogin("admin");
        admin.setPassword("admin");
        admin.setId(1);

        admin.setRoles(roles);
        em.persist(admin);
        em.getTransaction().commit();
    }

}
