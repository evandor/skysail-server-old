package de.twenty11.skysail.server.listener;

import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.servicedefinitions.LogValidationInputProvider;

/**
 * 
 * 
 * @author carsten
 * 
 */
public class LogValidationInputProviderServiceListener implements ServiceListener {

    /**
     * the osgi bundle context provided in the constructor.
     */
    private final BundleContext bundleContext;

    /**
     * slf4j based logging implementation.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** keeps track of the messages to expect when examining the log. */
    private Properties expectedMessages = new Properties();

    /**
     * Constructor which needs the bundleContext.
     * 
     * When this Listener is instantiated, the bundleContext is queried for all
     * the already existing serviceReferences to the
     * de.twenty11.skysail.server.UrlMapper class. The mappings provided by
     * those classes are then added to the applications router. This is done to
     * make sure that all the mappings from running bundles are taken into
     * account even though the restletosgi bundle might have been started later.
     * 
     * @param context
     *            the osgi bundle context
     * @param restletApp
     *            the restletOsgiApplication
     */
//    public LogValidationInputProviderServiceListener(final BundleContext context) {
//        // this(context, restletApp, false);
//        this.bundleContext = context;
//    }

    /**
     * Constructor which needs the bundleContext and the RestletOsgiApplication.
     * 
     * When this Listener is instantiated, the bundleContext is queried for all
     * the already existing serviceReferences to the
     * de.twenty11.skysail.server.UrlMapper class. The mappings provided by
     * those classes are then added to the applications router. This is done to
     * make sure that all the mappings from running bundles are taken into
     * account even though the restletosgi bundle might have been started later.
     * 
     * @param context
     *            the osgi bundle context
     * @param restletApp
     *            the restletOsgiApplication
     */
    public LogValidationInputProviderServiceListener(final BundleContext context) {
        logger.debug("instantiating new LogValidationInputProviderServiceListener");
        this.bundleContext = context;

        try {
            ServiceReference[] allLogValidationInputProvider = context.getAllServiceReferences(
                            LogValidationInputProvider.class.getName(), null);
            if (allLogValidationInputProvider != null) {
                for (ServiceReference serviceReference : allLogValidationInputProvider) {
                    addPropertiesFromBundle(context, serviceReference);
                }
            }
            logger.debug("adding new UrlMappingServiceListener to bundleContext");
            context.addServiceListener(this, "(objectClass=" + LogValidationInputProvider.class.getName() + ")");
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("invalid syntax", e);
        }
    }

    /**
     * This method adds, removes or modifies the URL mappings of the
     * applications router according to the service events fired.
     * 
     * @param event
     *            the serviceEvent
     */
    @Override
    public final void serviceChanged(final ServiceEvent event) {
        if (event.getType() == ServiceEvent.REGISTERED) {
            addPropertiesFromBundle(bundleContext, event.getServiceReference());
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            removeMapping(bundleContext, event.getServiceReference());
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            removeMapping(bundleContext, event.getServiceReference());
            addPropertiesFromBundle(bundleContext, event.getServiceReference());
        }
    }

    /**
     * All the provided URL mappings (from the serviceReference implementing the
     * UrlMapper interface) are attached to the applications router.
     * 
     * Possible exceptions are translated into runtime exceptions.
     * 
     * @param context
     *            the bundleContext provided to the constructor
     * @param serviceReference
     *            the service reference to a UrlMapper.
     */
    private void addPropertiesFromBundle(final BundleContext context, final ServiceReference serviceReference) {
        LogValidationInputProvider lvip = (LogValidationInputProvider) context.getService(serviceReference);
        Properties expectedMessagesFromBundle = lvip.getExpectedMessages();
        if (expectedMessagesFromBundle == null) {
            return;
        }
        expectedMessages.putAll(expectedMessagesFromBundle);
    }

    /**
     * All the provided URL mappings (from the serviceReference implementing the
     * UrlMapper interface) are removed from the applications router.
     * 
     * Possible exceptions are translated into runtime exceptions.
     * 
     * @param bundleContext
     * 
     * @param serviceRef
     *            the service reference to a UrlMapper.
     */
    private void removeMapping(BundleContext context, final ServiceReference serviceReference) {
        LogValidationInputProvider lvip = (LogValidationInputProvider) context.getService(serviceReference);
        Properties expectedMessagesFromBundle = lvip.getExpectedMessages();
        if (expectedMessagesFromBundle == null) {
            return;
        }
        for (Object ident : expectedMessagesFromBundle.keySet()) {
            expectedMessages.remove(expectedMessagesFromBundle.get(ident));
        }
    }

}
