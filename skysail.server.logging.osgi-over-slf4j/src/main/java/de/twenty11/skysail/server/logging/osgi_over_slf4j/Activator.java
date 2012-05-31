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

package de.twenty11.skysail.server.logging.osgi_over_slf4j;

import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class Activator implements BundleActivator {

    private static BundleContext context;
    private static final Logger logger = LoggerFactory.getLogger("skysail.server.osgi.logger.osgi-over-slf4j");

    private LogServiceTracker logServiceTracker;

    static BundleContext getContext() {
        return context;
    }
    
    private class LogServiceTracker extends ServiceTracker {

        public LogServiceTracker(final BundleContext context, final Filter filter) {
            super(context,filter,null);
        }
        
        @Override
        public Object addingService(ServiceReference reference) {
            Object service = context.getService(reference);
            if (service instanceof ExtendedLogReaderService) {
                ((ExtendedLogReaderService)service).addLogListener(new XLogListener());
            } else {
                ((LogReaderService)service).addLogListener(new NLogListener());
            }
            return super.addingService(reference);
        }
    }

    
    private class NLogListener implements LogListener {

        private final Logger logger = LoggerFactory.getLogger("skysail.server.osgi.logger.osgi-over-slf4j");

        @Override
        public void logged(LogEntry entry) {
            logLogEntry (logger,
                    entry.getLevel(),
                    LogServiceUtil.createBundleAndServiceMarker(entry, getContext()),
                    entry.getMessage(),
                    entry.getException());

        }

    }
    
    private class XLogListener implements LogListener {

        private final Logger logger = LoggerFactory.getLogger("skysail.server.osgi.logger.osgi-over-slf4j");

        @Override
        public void logged(LogEntry entry) {
            ExtendedLogEntry extendedEntry = (ExtendedLogEntry) entry;
            Logger xLogger = LoggerFactory.getLogger(ConstantsAndProperties.EXTENDED_LOGENTRY_PREFIX
                    + extendedEntry.getLoggerName());
//            logLogEntry (logger,
//                    entry.getLevel(),
//                    LogServiceUtil.createBundleAndServiceMarker(entry, getContext()),
//                    entry.getMessage(),
//                    entry.getException());
            

        }

    }


    private void logLogEntry (final Logger logger, final int level, final Marker marker, final String message,
            final Throwable e) {
        switch (level) {
        case LogService.LOG_DEBUG:
            logger.debug(marker, message, e);
            break;
        case LogService.LOG_ERROR:
            logger.error(marker, message, e);
            break;
        case LogService.LOG_INFO:
            logger.info(marker, message, e);
            break;
        case LogService.LOG_WARNING:
            logger.warn(marker, message, e);
            break;

        default:
            break;
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        
        // http://web.mac.com/ekkehard.gentz/ekkes-corner/blog/Eintr%C3%A4ge/2008/10/3_Logging_in_OSGI_Enterprise_Anwendungen%2C_Teil_2.html
        org.slf4j.bridge.SLF4JBridgeHandler.install();

        Filter logFilter = context
                .createFilter("(|(objectClass=org.osgi.service.log.LogReaderService)(objectClass=org.eclipse.equinox.log.ExtendedLogReaderService))");
        logServiceTracker = new LogServiceTracker(context, logFilter);
        logServiceTracker.open();

        context.addBundleListener(new SkysailBundleListener());
        context.addFrameworkListener(new SkysailFrameworkListener());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext bundleContext) throws Exception {
        Activator.context = null;
        logServiceTracker.close();
        logServiceTracker = null;
    }

}
