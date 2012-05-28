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

package de.twenty11.skysail.server.eclipselink;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The bundles activator, taking care of creating a new restlet component,
 * application and url mapping listener.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    private static final String PU_NAME = "SkysailDefaultPU";
    private EntityManagerFactory emf;
    private EntityManager em;
    
    /**
     * bundles life cycle start method, creating a new restlet component, a
     * restlet application and the urlMapping listener.
     * 
     * @param context
     *            the osgi bundle context
     * @throws Exception
     *             starting the component may trigger an Exception
     * 
     */
    public final void start(final BundleContext context) throws Exception {

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
            emf = new PersistenceProvider().createEntityManagerFactory(
                    PU_NAME, 
                    properties);
        }
        return emf;
    }

    /**
     * stop lifecycle method, removing the urlMapping listener and stopping the
     * component.
     * 
     * @param context
     *            the bundleContext
     * @throws Exception
     *             stopping the component might trigger an exception
     * 
     */
    public final void stop(final BundleContext context) throws Exception {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(new LoginEvent());
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

}
