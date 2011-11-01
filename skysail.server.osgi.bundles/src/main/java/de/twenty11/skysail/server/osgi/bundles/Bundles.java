package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.messages.GridInfo;
import de.twenty11.skysail.server.UrlMapper;
import de.twenty11.skysail.server.osgi.SkysailUtils;

public class Bundles implements UrlMapper {

    private BundleContext bundleContext;

    public static final String RESTLET_BUNDLE_CONTEXT_ID = "bundles";
    public static final String RESTLET_BUNDLE_NAME_ID = "bundleName";
    private static final String[] fields = { "ID","symbolicName", "version", "status" };

    @Override
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/", BundlesResource.class.getName());
        queue.put("/" + RESTLET_BUNDLE_CONTEXT_ID + "/{" + RESTLET_BUNDLE_NAME_ID + "}", BundleResource.class.getName());
        return queue;
    }

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

//    public List<LinkData> getBundles() {
//        Bundle[] bundles = bundleContext.getBundles();
//        List<LinkData> results = new ArrayList<LinkData>();
//        for (Bundle bundle : bundles) {
//            LinkData link = new LinkData();
//            link.setHref("bundles/" + bundle.getSymbolicName());
//            link.setValue(bundle.getSymbolicName());
//            results.add(link);
//        }
//        return results;
//
//    }

    public GridData getBundles() {
        Bundle[] bundles = bundleContext.getBundles();
        GridInfo fieldsList = SkysailUtils.createFieldList(fields);
        GridData grid = new GridData(fieldsList.getColumns());
        for (Bundle bundle : bundles) {
            RowData col = new RowData();
            List<Object> cols = new ArrayList<Object>();
            cols.add(bundle.getBundleId());
            cols.add(bundle.getSymbolicName());
            cols.add(bundle.getVersion());
            cols.add(bundle.getState());
            col.setColumnData(cols);
            grid.addRowData(col);
        }
        return grid;

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

}
