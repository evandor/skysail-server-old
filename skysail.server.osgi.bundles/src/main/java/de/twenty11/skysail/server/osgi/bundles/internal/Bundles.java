package de.twenty11.skysail.server.osgi.bundles.internal;

import java.util.Arrays;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.PackageAdmin;

public class Bundles {

    private BundleContext bundleContext;

    private static final String[] fields = { "ID","symbolicName", "version", "status" };

    private Bundles() {
    }

    /**
     * SingletonHolder is loaded on the first execution of
     * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
     * not before.
     */
    private static class SingletonHolder {
        public static final Bundles instance = new Bundles();
    }

    public static Bundles getInstance() {
        return SingletonHolder.instance;
    }

    public List<Bundle> getBundles() {
        Bundle[] bundles = bundleContext.getBundles();
        return Arrays.asList(bundles);
    }

    public void setContext(BundleContext context) {
        this.bundleContext = context;
    }

    public Bundle getBundle(String bundleName) {
        Bundle[] bundles = this.bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals(bundleName)) {
                return bundle;
            }
        }
        return null;
    }

    public Bundle getBundle(long id) {
        return this.bundleContext.getBundle(id);
    }

    public ServiceReference getServiceReference(String type) {
        return this.bundleContext.getServiceReference(type);
    }

    public Object getService(ServiceReference reference) {
        return this.bundleContext.getService(reference);
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
