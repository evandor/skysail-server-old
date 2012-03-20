package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Arrays;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

public class BundleUtils {

    /**
     * Singleton
     */
    private BundleUtils() {
    }

    /**
     * SingletonHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
     * not before.
     */
    private static class SingletonHolder {
        public static final BundleUtils instance = new BundleUtils();
    }

    public static BundleUtils getInstance() {
        return SingletonHolder.instance;
    }

    public List<Bundle> getBundles() {
        Bundle[] bundles = FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundles();
        return Arrays.asList(bundles);
    }

    public Bundle getBundle(String bundleName) {
        Bundle[] bundles = FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals(bundleName)) {
                return bundle;
            }
        }
        return null;
    }

    public Bundle getBundle(long id) {
        return FrameworkUtil.getBundle(this.getClass()).getBundleContext().getBundle(id);
    }

    public ServiceReference getServiceReference(String type) {
        return FrameworkUtil.getBundle(this.getClass()).getBundleContext().getServiceReference(type);
    }

    public Object getService(ServiceReference reference) {
        return FrameworkUtil.getBundle(this.getClass()).getBundleContext().getService(reference);
    }

    public static String translateStatus(int state) {
        // state as defined in org.osgi.framework.Bundle
        switch (state) {
            case Bundle.ACTIVE :
                return "ACTIVE";
            case Bundle.INSTALLED :
                return "INSTALLED";
            case Bundle.RESOLVED :
                return "RESOLVED";
            case Bundle.STARTING :
                return "STARTING";
            case Bundle.STOPPING :
                return "STOPPING";
            case Bundle.UNINSTALLED :
                return "UNINSTALLED";
            default :
                return "unknown (" + state +")";
        }
    }


    
}
