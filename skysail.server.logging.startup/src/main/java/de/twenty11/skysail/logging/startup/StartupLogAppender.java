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

package de.twenty11.skysail.logging.startup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * the idea: this is a logback appender, which will log the first n messages of
 * a bundle - those messages can afterwards be compared to a set of
 * what-was-expected and what-should-not-happen patterns provided by the actual
 * bundle.
 * 
 * As a result, a bundle can provide a list of patterns to check if things went
 * according to plan when it was started. If the bundle gets uninstalled again,
 * the logs are cleared. Only INFO-, WARNING- and ERROR-level events will be
 * logged at all.
 * 
 * Config: As this extends the logback appender base, the class has to be
 * configured in logback(-text).xml, e.g. <appender name="STARTUP"
 * class="de.twenty11.skysail.logging.startup.StartupLogAppender" /> In skysail,
 * this file can be found in skysail.server.osgi.logback.config
 * 
 * @author carsten
 * 
 */
public class StartupLogAppender extends AppenderBase<ILoggingEvent> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Helper class to keep track of LoggingEvents.
     * 
     * @author carsten
     * 
     */
    private class LogEventsHolder {

        /**
         * maximum number of entries to keep track of. It doesn't matter if the
         * events are warnings, info or errors, the number of all events is
         * taken into account.
         */
        private int max = 50; // default

        /** container for info events. */
        private List<ILoggingEvent> infoLog = new ArrayList<ILoggingEvent>();

        /** container for warning events. */
        private List<ILoggingEvent> warningLog = new ArrayList<ILoggingEvent>();

        /** container for error events. */
        private List<ILoggingEvent> errorLog = new ArrayList<ILoggingEvent>();

        /** thread-safe counter. */
        private AtomicInteger counter = new AtomicInteger(0);

        /**
         * constructor allown overwriting the default for "max".
         * 
         * @param max
         *            maximum number of events to track
         */
        public LogEventsHolder(final int max) {
            this.max = max;
        }

        public void log(ILoggingEvent event) {

            // logs are full? then return...
            if (counter.intValue() > max) {
                return;
            }

            Level level = event.getLevel();
            if (level.equals(Level.INFO)) {
                infoLog.add(event);
            } else if (level.equals(Level.WARN)) {
                warningLog.add(event);
            } else if (level.equals(Level.ERROR)) {
                errorLog.add(event);
            }
            counter.addAndGet(1);
        }

        public List<ILoggingEvent> getInfoLog() {
            return infoLog;
        }

        public List<ILoggingEvent> getWarningLog() {
            return warningLog;
        }

        public List<ILoggingEvent> getErrorLog() {
            return errorLog;
        }
    }

    /**
     * Map between bundle-SymbolicName and a list of log entries.
     */
    private static Map<String, LogEventsHolder> logs = new HashMap<String, LogEventsHolder>();

    /**
     * default constructor.
     */
    public StartupLogAppender() {
        // eat your own dogfood
        logger.info("starting the StartupLogAppender");
    }

    @Override
    protected void append(final ILoggingEvent event) {
        LogEventsHolder startupLog = null;
        String loggerName = event.getLoggerName();
        if (logs.containsKey(loggerName)) {
            startupLog = logs.get(loggerName);
        } else {
            startupLog = new LogEventsHolder(50);
            logs.put(loggerName, startupLog);
        }
        startupLog.log(event);
    }

    /**
     * @param bundleName
     * @return
     */
    public static List<ILoggingEvent> getInfoLog(String bundleName) {
        LogEventsHolder logList = logs.get(bundleName);
        if (logList == null)
            return null;
        return logList.getInfoLog();
    }

    /**
     * @param bundleName
     * @return
     */
    public static List<ILoggingEvent> getWarningLog(String bundleName) {
        LogEventsHolder logList = logs.get(bundleName);
        if (logList == null)
            return null;
        return logList.getWarningLog();
    }

    /**
     * @param bundleName
     * @return
     */
    public static List<ILoggingEvent> getErrorLog(String bundleName) {
        LogEventsHolder logList = logs.get(bundleName);
        if (logList == null)
            return null;
        return logList.getErrorLog();
    }

    public static Set<String> getLoggerNames() {
        return logs.keySet();
    }

}
