package de.twenty11.skysail.logging.startup.internal;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import de.twenty11.skysail.logging.startup.StartupLogAppender;
import edu.umd.cs.findbugs.annotations.NoWarning;

/**
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** slf4j based logger. */
    private static Logger logger = LoggerFactory.getLogger(Activator.class);

    /** OSGi bundle context . */
    private static BundleContext context;

//    /** listener taking care of log validation input. */
//    private static LogValidationInputProviderServiceListener logValServiceListener;

    public static BundleContext getContext() {
        return Activator.context;
    }
    
//    public static Properties getExpectedLogMessages() {
//        return logValServiceListener.getExpectedMessages();
//    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    @NoWarning("findbug warning not relevant here")
    public final void start(final BundleContext context) throws Exception {
        Activator.context = context;
//        logValServiceListener = new LogValidationInputProviderServiceListener(context);
        // Bundles.getInstance().setContext(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    @NoWarning("findbug warning not relevant here")
    public final void stop(final BundleContext context) throws Exception {
        Activator.context = null;

//        // these are the loggers something was logged to during startup
//        Set<String> loggerNames = StartupLogAppender.getLoggerNames();
//        // these are the messages we expect to find in the logs
//        Properties expectedMessages = logValServiceListener.getExpectedMessages();
//
//        // first make sure we have logs for all expected messages
//        checkLogExistsForAllExpectedMessages(loggerNames, expectedMessages);
//
//        dumpResults(loggerNames, expectedMessages);
//
//        logValServiceListener = null;
    }


    

}
