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
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * the idea: this is a logback appender, which will log the first n messages of a bundle - those messages 
 * can afterwards be compared to a set of what-was-expected and what-should-not-happen patterns provided by
 * the actual bundle.
 * 
 * As a result, a bundle can provide a list of patterns to check if things went according to plan when it
 * was started. If the bundle gets uninstalled again, the logs are cleared. Only INFO-, WARNING- and ERROR-level
 * events will be logged at all.
 * 
 * @author carsten
 *
 */
public class StartupLogAppender extends AppenderBase<ILoggingEvent> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private class LogList {
        
        private int max = 50;

        private List<ILoggingEvent> infoLog = new ArrayList<ILoggingEvent>();
        private List<ILoggingEvent> warningLog = new ArrayList<ILoggingEvent>();
        private List<ILoggingEvent> errorLog = new ArrayList<ILoggingEvent>();
        
        private AtomicInteger counter = new AtomicInteger(0);
        
        public LogList(int max) {
            this.max  = max;
        }

        public void log(ILoggingEvent event) {
            
            // logs are full?
            if (counter.intValue() > max)
                return;
            
            Level level = event.getLevel();
            if (level.equals(Level.INFO)) {
                infoLog.add(event);
            }
            else if (level.equals(Level.WARN)) {
                warningLog.add(event);                
            }
            else if (level.equals(Level.ERROR)) {
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
    
    private static Map<String, LogList> loggerMaps = new HashMap<String, LogList>();
    
    public StartupLogAppender() {
        // eat your own dogfood
        logger.info("starting the StartupLogAppender");
    }

    @Override
    protected void append(ILoggingEvent event) {
        LogList bundleLog = null;
        String bundleName = event.getLoggerName();
        if (loggerMaps.containsKey(bundleName)) {
            bundleLog = loggerMaps.get(bundleName);
        } else {
            bundleLog = new LogList(50);
            loggerMaps.put(bundleName, bundleLog);
        }
        bundleLog.log(event);
    }
    
    public static List<ILoggingEvent> getInfoLog(String bundleName) {
        LogList logList = loggerMaps.get(bundleName);
        if (logList == null)
            return null;
        return logList.getInfoLog();
    }

    public static List<ILoggingEvent> getWarningLog(String bundleName) {
        LogList logList = loggerMaps.get(bundleName);
        if (logList == null)
            return null;
        return logList.getWarningLog();
    }

    public static List<ILoggingEvent> getErrorLog(String bundleName) {
        LogList logList = loggerMaps.get(bundleName);
        if (logList == null)
            return null;
        return logList.getErrorLog();
    }

}
