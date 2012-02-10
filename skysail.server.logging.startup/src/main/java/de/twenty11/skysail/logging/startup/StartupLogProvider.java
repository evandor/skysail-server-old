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

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

public class StartupLogProvider implements StartupLogProviderService {

    @Override
    public Status getStartupStatus(String bundleName, List<Pattern> expected, List<Pattern> unexpected) {
        
        // do we have errors?
        List<ILoggingEvent> errorLog = StartupLogAppender.getErrorLog(bundleName);
        if (errorLog != null) {
            if (errorLog.size() != 0) {
                return Status.FAIL;
            }
        }

        // do we have warnings?
        List<ILoggingEvent> warningLog = StartupLogAppender.getWarningLog(bundleName);
        if (warningLog != null) {
            if (warningLog.size() != 0) {
                return Status.WARNING;
            }
        }
        
        // do we have unexpected (info-level) logs?
        for (Pattern unexpectedPattern : unexpected) {
            List<ILoggingEvent> infoLog = StartupLogAppender.getInfoLog(bundleName);
            if (infoLog != null) {
                for (ILoggingEvent event : infoLog) {
                    String message = event.getFormattedMessage();
                    Matcher matcher = unexpectedPattern.matcher(message);
                    if (matcher.find()) {
                        return Status.FAIL;
                    }
                }
            }
        }

        // do we have all expected (info-level) logs?
        for (Pattern expectedPattern : expected) {
            List<ILoggingEvent> infoLog = StartupLogAppender.getInfoLog(bundleName);
            
            if (infoLog == null && expected.size() > 0)
                return Status.FAIL;

            boolean fail = false;
            if (infoLog != null) {
                for (ILoggingEvent event : infoLog) {
                    String message = event.getFormattedMessage();
                    Matcher matcher = expectedPattern.matcher(message);
                    if (!matcher.find()) {
                        fail = true;
                    }
                }
                if (fail) {
                    return Status.FAIL;
                }
            }
        }

        return Status.OK;
    }

    @Override
    public Set<String> getBundleNames() {
        return StartupLogAppender.getLoggerNames();
    }

}
