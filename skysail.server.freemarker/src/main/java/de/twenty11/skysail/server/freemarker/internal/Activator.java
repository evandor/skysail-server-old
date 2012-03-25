package de.twenty11.skysail.server.freemarker.internal;

import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.freemarker.FtlTrackerCustomizer;
import freemarker.template.Configuration;

/**
 * A bundle checking other bundles for specific contributions ("FtlTemplates")
 * which is providing a service for consumers to access those contributions
 * (which are freemarker template files).
 *
 * Idea as described here:
 * http://eclipse.dzone.com/articles/osgi-42-extender-pattern-and
 *
 * @author carsten
 *
 */
public class Activator implements BundleActivator {

    /** slf4j based logger. */
    private static Logger       logger = LoggerFactory.getLogger(Activator.class);

    /** OSGi bundle bundleTracker. */
    private BundleTracker       bundleTracker;

    /** OSGi service serviceRegistration. */
    private ServiceRegistration serviceRegistration;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    public final void start(final BundleContext context) throws Exception {

        logger.info("starting bundle " + context.getBundle().getSymbolicName());

        // try to find an available freemarker configuration via OSGi services
        ServiceReference[] serviceReferences = context.getServiceReferences(Configuration.class.getName(),
                "(dynamicConfiguration=true)");
        FtlTrackerCustomizer customizer = createCustomizer(context, serviceReferences);

        registerFreemarkerConfigurationService(context, customizer);
        bundleTracker = startTracking(context, customizer);
        processBundles(context, customizer);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public final void stop(final BundleContext context) throws Exception {
        if (serviceRegistration != null) {
            serviceRegistration.unregister();
        }
        if (bundleTracker != null) {
            bundleTracker.close();
        }
    }

    /**
     * process bundles started before I started listening for bundles.
     * 
     * @param context
     *            bundle context
     * @param customizer
     *            bundle tracker customizer
     */
    private void processBundles(final BundleContext context, final FtlTrackerCustomizer customizer) {
        // process other bundles
        for (Bundle b : context.getBundles()) {
            if (b.getState() == Bundle.ACTIVE || b.getState() == Bundle.RESOLVED) {
                customizer.addingBundle(b, null);
            }
        }
        // process this bundle itself
        customizer.addingBundle(context.getBundle(), null);
    }

    /**
     * @param context
     *            context
     * @param serviceReferences
     *            service references
     * @return customizer
     */
    private FtlTrackerCustomizer createCustomizer(final BundleContext context,
            final ServiceReference[] serviceReferences) {

        Configuration freemarkerConfig = null;
        if (serviceReferences != null && serviceReferences.length > 0) {
            logger.debug("found {} existing freemarker configurations; choosing the first one",
                    serviceReferences.length);
            freemarkerConfig = (Configuration) context.getService(serviceReferences[0]);
            return new FtlTrackerCustomizer(freemarkerConfig);
        } else {
            return new FtlTrackerCustomizer();
        }
    }

    /**
     * @param context
     *            bundleContext
     * @param customizer
     *            for the bundle tracker
     * @return the new bundle tracker created
     */
    private BundleTracker startTracking(final BundleContext context, final FtlTrackerCustomizer customizer) {
        bundleTracker = new BundleTracker(context, Bundle.RESOLVED, customizer);
        bundleTracker.open();
        return bundleTracker;
    }

    /**
     * @param context
     *            bundle context
     * @param customizer
     *            bundle tracker customizer
     */
    private void registerFreemarkerConfigurationService(final BundleContext context,
            final FtlTrackerCustomizer customizer) {
        Hashtable<String, String> props = new Hashtable<String, String>(1);
        props.put("dynamicConfiguration", "true");
        serviceRegistration = context.registerService(Configuration.class.getName(),
                customizer.getFreemarkerConfiguration(), props);
    }

}
