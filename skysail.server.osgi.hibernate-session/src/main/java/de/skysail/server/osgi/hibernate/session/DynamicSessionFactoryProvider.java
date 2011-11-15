package de.skysail.server.osgi.hibernate.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import de.skysail.server.osgi.hibernate.session.internal.BundleTracker;
import de.skysail.server.osgi.hibernate.session.internal.HibernateSessionActivator;

/**
 * Hibernate session factory that can get updated during the runtime of the
 * application.
 * 
 * The necessary information is derived from provided hibernate properties as
 * well as from a list of annotated classes which are taken into account.
 * 
 * The list of annotated classes can be changed on runtime, this is why this
 * class is called DynamicSessionFactoryProvider.
 * 
 */
public class DynamicSessionFactoryProvider implements InitializingBean, SessionFactoryProvider {

    private Logger logger = LoggerFactory.getLogger(DynamicSessionFactoryProvider.class);

    /**
     * list of annotated classes which are meant to be used in hibernate session
     * factory
     */
    private Set<Class<?>> annotatedClasses = new HashSet<Class<?>>();

    /** hibernate properties to build hibernate session factory */
    private Properties hibernateProperties;

    // TODO check this - we need a singleton DynamicSessionFactoryProvider?
    private static SessionFactory proxiedSessionFactory;

    public DynamicSessionFactoryProvider() {
        System.out.println("new dynamicSessionFactoryProvider instantiated");
    }

    /**
     * @return
     */
    @Override
    public SessionFactory getSessionFactory() {
        return proxiedSessionFactory;
    }

    public void afterPropertiesSet() throws Exception {
        createNewSessionFactory();
        BundleTracker.setDynamicConfiguration(this);
        // HibernateSessionActivator.setDynamicConfiguration(this);
    }

    /**
     * @param hibernateProperties
     */
    public void setHibernateProperties(Properties hibernateProperties) {
        this.hibernateProperties = hibernateProperties;
    }

    /**
     * @param annotatedClasses
     */
    public void setAnnotatedClasses(Set<Class<?>> annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
    }

    public void addAnnotatedClass(Class<?> anotadedClass) {
        annotatedClasses.add(anotadedClass);
        createNewSessionFactory();
    }

    public void removeAnnotatedClass(Class<?> anotadedClass) {
        annotatedClasses.remove(anotadedClass);
        createNewSessionFactory();
    }

    public void addAnnotatedClasses(Bundle sourceBundle, BundleContext bundleContext, String[] classes) {
        for (String s : classes) {
            try {
                logger.info("Adding class: " + s);
                annotatedClasses.add(sourceBundle.loadClass(s));
            } catch (ClassNotFoundException e) {
                checkOtherBundles(bundleContext, s, e);
            }
        }
        createNewSessionFactory();
    }

    private void checkOtherBundles(BundleContext bundleContext, String s, ClassNotFoundException e) {
        // class might not be in source bundle - try to check the other
        // bundles...
        boolean found = false;
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            try {
                annotatedClasses.add(bundle.loadClass(s));
                found = true;
                break;
            } catch (ClassNotFoundException e1) {
                // no error
            }
        }
        if (!found) {
            // now it is an error
            logger.error("Error adding annotaded class: " + s, e);
            throw new RuntimeException(e);
        }
    }

    public void removeAnnotatedClasses(Bundle sourceBundle, String[] classes) {
        for (String s : classes) {
            for (Class<?> c : annotatedClasses) {
                if (c.getName().equals(s)) {
                    logger.error("Removing class: " + s);
                    annotatedClasses.remove(c);
                    break;
                }
            }
        }
        createNewSessionFactory();
    }

    public void addAnnotatedClasses(Class<?>[] classes) {
        annotatedClasses.addAll(Arrays.asList(classes));
        createNewSessionFactory();
    }

    public void removeAnnotatedClasses(Class<?>[] classes) {
        annotatedClasses.removeAll(Arrays.asList(classes));
        createNewSessionFactory();
    }

    @Override
    public String toString() {
        return new StringBuffer("{").append(annotatedClasses.toString()).append("}").toString();
    }

    /**
     * central method of this class - creates the (dynamic) session factory
     * which can be retrieved using getSessionFactory.
     * 
     * The hibernate properties are taken into account as well as the current
     * set of annotated classes.
     */
    private void createNewSessionFactory() {

        logger.info("Creating new session factory...");

        if (hibernateProperties == null) {
            throw new IllegalStateException("Hibernate properties have not been set yet");
        }

        AnnotationConfiguration cfg = new AnnotationConfiguration();

        cfg.setProperties(hibernateProperties);
        for (Class<?> c : annotatedClasses) {
            cfg.addAnnotatedClass(c);
        }

        final SessionFactory sessionFactory = cfg.buildSessionFactory();

        proxiedSessionFactory = (SessionFactory) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[] { SessionFactory.class }, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        logger.info("Delegate factory invoked: " + sessionFactory + "" + method.getName());
                        return method.invoke(sessionFactory, args);
                    }
                });

        logger.info("Created new session factory: " + sessionFactory);
        logger.info("Known classes are");
        for (Class<?> c : annotatedClasses) {
            logger.info(c.getName());
        }
    }
}
