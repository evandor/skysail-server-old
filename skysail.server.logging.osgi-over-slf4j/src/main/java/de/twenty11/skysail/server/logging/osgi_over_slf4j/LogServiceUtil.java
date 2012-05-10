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

import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.ServiceEvent;
import org.osgi.service.log.LogEntry;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogServiceUtil {
    public static final String SERVICE_MARKER = "SERVICE";
    public static final String LOG_SERVICE_MARKER = "LOG_SERVICE";
    public static final String FRAMEWORK_MARKER = "FRAMEWORK";
    public static final String BUNDLE_MARKER = "BUNDLE";
    public static final String MARKER_SEPARATOR = "-";

    public static Marker createBundleAndServiceMarker(ServiceEvent pEvent, BundleContext p_bundleContext) {
        return MarkerFactory.getMarker(p_bundleContext.getBundle().getSymbolicName() + MARKER_SEPARATOR
                + SERVICE_MARKER);
    }

    public static Marker createBundleAndServiceMarker(BundleEvent p_event, BundleContext p_bundleContext) {
        return MarkerFactory
                .getMarker(p_bundleContext.getBundle().getSymbolicName() + MARKER_SEPARATOR + BUNDLE_MARKER);
    }

    public static Marker createBundleAndServiceMarker(FrameworkEvent p_event, BundleContext p_bundleContext) {
        return MarkerFactory.getMarker(p_bundleContext.getBundle().getSymbolicName() + MARKER_SEPARATOR
                + FRAMEWORK_MARKER);
    }

    public static Marker createBundleAndServiceMarker(LogEntry pEntry, BundleContext p_bundleContext) {
        return MarkerFactory.getMarker(p_bundleContext.getBundle().getSymbolicName() + MARKER_SEPARATOR
                + LOG_SERVICE_MARKER);
    }

}