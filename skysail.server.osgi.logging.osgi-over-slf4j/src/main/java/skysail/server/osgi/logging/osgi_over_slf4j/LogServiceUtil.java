package skysail.server.osgi.logging.osgi_over_slf4j;

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