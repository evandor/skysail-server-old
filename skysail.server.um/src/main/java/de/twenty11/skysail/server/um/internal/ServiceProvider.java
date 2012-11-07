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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * provides central access to the OSGi services.
 * 
 * @author carsten
 * 
 */
public class ServiceProvider {

    /** slf4j based logger. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private EntityManagerFactory emf;
    private EntityManager em;

    /**
     * Activating this component will check if the skysail database is setup correctly.
     * 
     * @param component
     *            provided by framework
     */
    protected void activate(final ComponentContext component) {
        logger.info("activating component in {}", component.getBundleContext().getBundle().getSymbolicName());
        try {
            EntityManager em = getEntityManager();
            // validateDefaultClient(em);
            validateDefaultRole(em);
            // validateDefaultPermission(em);
            validateDefaultUser(em);
            em.close();
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    // /**
    // * @return EntityManager or throws exception
    // */
    // private EntityManager getEntityManager() {
    // Properties properties = configService.getProperties("skysail.defaultDb.");
    // properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
    // EntityManagerFactory emf = new PersistenceProvider()
    // .createEntityManagerFactory("skysail.server.um", properties);
    // EntityManager em = emf.createEntityManager();
    // return em;
    // }

    /**
     * cleanup.
     * 
     * @param component
     *            provided by framework
     */
    protected void deactivate(final ComponentContext component) {
        logger.info("deactivating component in {}", component.getBundleContext().getBundle().getSymbolicName());
    }

    private void validateDefaultRole(EntityManager em) {
        try {

            em.getTransaction().begin();
            SkysailRole adminrole = new SkysailRole();
            adminrole.setRolename("administrator");
            adminrole.setId(1);
            em.persist(adminrole);
            em.getTransaction().commit();

        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    private void validateDefaultUser(EntityManager em) {
        try {
            SkysailRole role = new RoleService().getRole(1);
            Set<SkysailRole> roles = new HashSet<SkysailRole>();
            roles.add(role);

            em.getTransaction().begin();
            SkysailUser admin = new SkysailUser();
            admin.setLogin("admin");
            admin.setPassword("admin");
            admin.setId(1);

            admin.setRoles(roles);
            em.persist(admin);
            em.getTransaction().commit();

        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    private EntityManager getEntityManager() {
        if (em == null) {
            em = getEntityManagerFactory().createEntityManager();
        }
        return em;
    }

    private EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            HashMap properties = new HashMap();
            properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
            emf = new PersistenceProvider().createEntityManagerFactory("skysail.server.um", properties);
        }
        return emf;
    }

}
