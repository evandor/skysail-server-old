package de.skysail.server.osgi.hibernate.session.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.skysail.server.osgi.hibernate.session.DynamicSessionFactoryProvider;

public class HibernateSessionActivator implements BundleActivator{

    private EntityServiceTracker serviceTracker;

    /** provided via injection */
    private static DynamicSessionFactoryProvider dynamicConfiguration;

    private static Logger logger = LoggerFactory.getLogger(HibernateSessionActivator.class);

    private static HibernateSessionActivator instance;

    private BundleContext bundleContext;

    public HibernateSessionActivator() {
        System.out.println("new HibernateSessionActivator");
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        instance = this;
        bundleContext = context;
        serviceTracker = new EntityServiceTracker(context);
        serviceTracker.open();
        startTrackingBundles();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        serviceTracker.close();
        bundleContext = null;
        instance = null;
    }
    
    /**
     * Start tracking bundles after dynamic configuration has been initialized
     */
    private static void startTrackingBundles() {
        logger.info("Have dynamic configuration: "
                + (dynamicConfiguration != null));
        logger.info("Have instance: " + (instance != null));
        if (dynamicConfiguration != null && instance != null) {
            // now start tracking bundles
            logger.info("Starting to track bundle events");
            //instance.bundleContext.addBundleListener(instance);
//            instance.processBundles();
        }
    }

    public static void setDynamicConfiguration(
            DynamicSessionFactoryProvider dynamicConfiguration) {
        HibernateSessionActivator.dynamicConfiguration = dynamicConfiguration;
        startTrackingBundles();
    }

    /**
     * process bundles started before I started listening for bundles
     */
//    private void processBundles() {
//        for (Bundle b : instance.bundleContext.getBundles()) {
//            if (b.getState() == Bundle.ACTIVE) {
//                updateBundleClasses(b, BundleEvent.STARTED);
//            }
//        }
//    }

    static void updateBundleClasses(Bundle bundle, String[] classes, int updateAction) {
          switch (updateAction) {
          case ServiceEvent.REGISTERED:
              dynamicConfiguration.addAnnotatedClasses(bundle, null, classes);
              break;
          case ServiceEvent.UNREGISTERING:
              dynamicConfiguration.removeAnnotatedClasses(bundle, classes);
              break;
          default:
              throw new IllegalArgumentException("" + updateAction);
          }
    }

}
