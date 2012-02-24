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

package de.twenty11.skysail.logging.startup.internal;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;

import de.twenty11.skysail.logging.startup.StartupLogAppender;
import de.twenty11.skysail.server.servicedefinitions.LogValidationInputProvider;
import de.twenty11.skysail.server.serviceprovider.SkysailServiceProvider;

public class ServiceProvider extends SkysailServiceProvider implements LogValidationInputProvider {

    /** slf4j based logger implementation */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** listener taking care of log validation input. */
    private static LogValidationInputProviderServiceListener logValServiceListener;
    
    @Override
    public Properties getExpectedMessages() {
        URL entry = Activator.getContext().getBundle().getEntry("validation/expecedLogContents.properties");
        Properties props = new Properties();
        try {
            props.load(entry.openStream());
            return props;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    protected void activate(ComponentContext ctxt) {
        logValServiceListener = new LogValidationInputProviderServiceListener(ctxt.getBundleContext());
    }

    protected void deactivate(ComponentContext ctxt) {
        logValServiceListener = null;
    }
    
    public void dumpResults(Set<String> loggerNames, Properties expectedMessages) {
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
    public void checkLogExistsForAllExpectedMessages(final Set<String> loggerNames, final Properties expectedMessages) {
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


}
