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
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.servicedefinitions.ConfigService;

/**
 * provides central access to the OSGi services.
 * @author carsten
 *
 */
public class ServiceProvider {

    /** OSGi-provided configuration service. */
    private static ConfigService configService;
    
    /** slf4j based logger. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Activating this component will check if the skysail database is setup correctly.
     * 
     * @param component provided by framework
     */
    protected void activate(final ComponentContext component) {
        logger.info("activating component in {}", component.getBundleContext().getBundle().getSymbolicName());
        try {
            EntityManager em = getEntityManager();
            validateDefaultClient(em);
            validateDefaultRole(em);
            validateDefaultPermission(em);
            validateDefaultUser(em);
            em.close();
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    /**
     * @return EntityManager or throws exception
     */
    private EntityManager getEntityManager() {
        Properties properties = configService.getProperties("skysail.defaultDb.");
        properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
        EntityManagerFactory emf = new PersistenceProvider().createEntityManagerFactory("skysail.server.um",
                        properties);
        EntityManager em = emf.createEntityManager();
        return em;
    }
    
    /**
     * cleanup. 
     * @param component provided by framework
     */
    protected void deactivate(final ComponentContext component) {
        logger.info("deactivating component in {}", component.getBundleContext().getBundle().getSymbolicName());
    }
    
    /**
     * set by framework.
     * @param service provided config service
     */
    public final synchronized void setConfigService(final ConfigService service) {
        ServiceProvider.configService = service;
    }
    
    /**
     * @return the config service provided by the framework.
     */
    public static ConfigService getConfigService() {
        return configService;
    }
    
    private void validateDefaultClient(EntityManager em) {
        try {
            em.getTransaction().begin();
            SkysailClient client = new SkysailClient();
            client.setId(1);
            client.setClientname("default");
            em.persist(client);
            em.getTransaction().commit();
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    private void validateDefaultRole(EntityManager em) {
        try {
            SkysailClient defaultClient = ClientService.getClient(1);
            
            em.getTransaction().begin();
            SkysailRole adminrole = new SkysailRole();
            adminrole.setRolename("administrator");
            adminrole.setId(1);
            adminrole.setClient(defaultClient);
            em.persist(adminrole);
            em.getTransaction().commit();
            
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    private void validateDefaultPermission(EntityManager em) {
        try {
            SkysailClient defaultClient = ClientService.getClient(1);
            
            em.getTransaction().begin();
            SkysailPermission masterPermission = new SkysailPermission();
            masterPermission.setPermissionName("masterpermission");
            masterPermission.setId(1);
            masterPermission.setClient(defaultClient);
            em.persist(masterPermission);
            em.getTransaction().commit();
            
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

    private void validateDefaultUser(EntityManager em) {
        try {
            SkysailRole role = RoleService.getRole(1);
            Set<SkysailRole> roles = new HashSet<SkysailRole>();
            roles.add(role);
            
            SkysailClient defaultClient = ClientService.getClient(1);
            Set<SkysailClient> clients = new HashSet<SkysailClient>();
            clients.add(defaultClient);

            SkysailPermission masterPermission = PermissionService.getPermission(1);
            Set<SkysailPermission> permissions = new HashSet<SkysailPermission>();
            permissions.add(masterPermission);

            em.getTransaction().begin();
            SkysailUser admin = new SkysailUser();
            admin.setLogin("admin");
            admin.setPassword("admin");
            admin.setId(1);
            admin.setClients(clients);
            admin.setPermissions(permissions);
           
            admin.setRoles(roles);
            em.persist(admin);
            em.getTransaction().commit();
            
        } catch (Throwable t) {
            logger.error("Persisting Entity threw error", t);
        }
    }

}
