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

package de.twenty11.skysail.server.internal;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceListener;

import de.twenty11.skysail.server.LoginEvent2;
import de.twenty11.skysail.server.listener.SkysailServerBundleListener;
import de.twenty11.skysail.server.listener.SkysailServerFrameworkListener;
import de.twenty11.skysail.server.listener.SkysailServerServiceListener;

/**
 * The bundles activator.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** slf4j based logger implementation. */
    // private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** a skysail server global framework listener. */
    private FrameworkListener frameworkLister = new SkysailServerFrameworkListener();

    /** a skysail server global bundle listener. */
    private BundleListener bundleListener = new SkysailServerBundleListener();

    /** a skysail server global bundle listener. */
    private ServiceListener serviceListener = new SkysailServerServiceListener();

    private EntityManagerFactory emf;
    private EntityManager em;

    /** {@inheritDoc} */
    public final void start(final BundleContext context) throws Exception {
        // add framework event listener for all skysail server components
        context.addFrameworkListener(frameworkLister);
        // same for bundles
        context.addBundleListener(bundleListener);
        // ... and services
        context.addServiceListener(serviceListener);
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
            emf = new PersistenceProvider().createEntityManagerFactory("SkysailPU", properties);
        }
        return emf;
    }

    /** {@inheritDoc} */
    public final void stop(final BundleContext context) throws Exception {

        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(new LoginEvent2());
        em.getTransaction().commit();
        em.close();
        emf.close();

        // clean up
        context.removeFrameworkListener(frameworkLister);
        context.removeBundleListener(bundleListener);
        context.removeServiceListener(serviceListener);
    }

}
