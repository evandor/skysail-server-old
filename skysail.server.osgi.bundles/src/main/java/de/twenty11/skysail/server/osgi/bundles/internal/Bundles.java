package de.twenty11.skysail.server.osgi.bundles.internal;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.messages.GridData;

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

//    public GridData getBundles(GridData grid, Filter filter) {
//        Bundle[] bundles = bundleContext.getBundles();
//        GridInfo fieldsList = SkysailUtils.createFieldList(fields);
//        int count = 0;
//        for (Bundle bundle : bundles) {
//            RowData col = new RowData();
//            List<Object> cols = new ArrayList<Object>();
//            cols.add(bundle.getBundleId());
//            cols.add(bundle.getSymbolicName());
//            cols.add(bundle.getVersion());
//            cols.add(translateStatus(bundle.getState()));
//            col.setColumnData(cols);
//            grid.addRowData(col);
//            count++;
//        }
//        grid.setAvailableRows(count);
//        return grid;
//
//    }

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

    private String translateStatus(int state) {
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

    public GridData getBundles(GridData grid, Object object) {
        // TODO Auto-generated method stub
        return null;
    }

    
}
