package de.twenty11.skysail.logging.startup.internal;

import java.util.Iterator;
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

    private LogValidationInputProviderServiceListener logValServiceListener;

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
        logValServiceListener = new LogValidationInputProviderServiceListener(context);
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

        // these are the loggers something was logged to during startup
        Set<String> loggerNames = StartupLogAppender.getLoggerNames();
        // these are the messages we expect to find in the logs
        Properties expectedMessages = logValServiceListener.getExpectedMessages();

        // first make sure we have logs for all expected messages
        checkLogExistsForAllExpectedMessages(loggerNames, expectedMessages);

        for (String loggerName : loggerNames) {
            List<ILoggingEvent> infoLog = StartupLogAppender.getInfoLog(loggerName);
            String expectedMessagesForLogger = (String) expectedMessages.get(loggerName);
            if (expectedMessagesForLogger == null) {
                continue;
            }

            Object[] loggedMessages = infoLog.toArray();

            String[] splitMsgsByComma = expectedMessagesForLogger.split(",");
            // need to find all expected message for this logger in the infoLog
            // for that logger
            for (String expectedMessage : splitMsgsByComma) {
                boolean found = false;
                iterateLoggedMessages : for (Object event : loggedMessages) {
                    if (((ILoggingEvent) event).getMessage().contains(expectedMessage.trim())) {
                        found = true;
                        break iterateLoggedMessages;
                    }
                }
                if (!found) {
                    logger.error("did not find expected message '{}' in the logs for logger '{}'", expectedMessage,
                                    loggerName);
                }
            }

        }

        logValServiceListener = null;
    }

    /**
     * We have a set of loggers (their names) and a properties object with
     * expected messages. The keySet of the properties should be a subset of the
     * available log names, otherwise a log message which was expected cannot be
     * found at all in the list of log messages.
     * 
     * @param loggerNames
     *            a set with all available logger names
     * @param expectedMessages
     *            mapping logger names with comma-separated list of expected
     *            message (fragments)
     */
    private void checkLogExistsForAllExpectedMessages(final Set<String> loggerNames, final Properties expectedMessages) {
        Set<Object> expectedLoggerNames = expectedMessages.keySet();
        if (!loggerNames.containsAll(expectedLoggerNames)) {
            for (Object expectedLogger : expectedLoggerNames) {
                if (!loggerNames.contains(expectedLogger)) {
                    logger.error("log does not contain entries for expected logger with name '{}'",
                                    expectedLogger.toString());
                }
            }
        }
    }

    public static BundleContext getContext() {
        return Activator.context;
    }

}
