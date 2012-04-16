package de.twenty11.skysail.server.osgi.bundles;

import java.util.List;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.BundleUtils;

/**
 * A grid-based resource for bundles.
 * 
 * @author carsten
 * 
 */
public class BundlesResource extends GridDataServerResource {

    public BundlesResource() {
        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public void configureColumns(final ColumnsBuilder builder) {
       builder.addColumn("id").setWidth(50).sortDesc(1);
       builder.addColumn("symbolicName").setWidth(350).setWidth(100);
       builder.addColumn("version").setWidth(100);
       builder.addColumn("state").setWidth(50);
    }

    @Override
    public void buildGrid() {
        List<Bundle> bundles = BundleUtils.getInstance().getBundles();
        Filter filter = getSkysailData().getFilter();
        for (Bundle bundle : bundles) {
            RowData rowData = new RowData(getSkysailData().getColumns());
            rowData.add(bundle.getBundleId());
            rowData.add(bundle.getSymbolicName());
            rowData.add(bundle.getVersion());
            rowData.add(BundleUtils.translateStatus(bundle.getState()));
            getSkysailData().addRowData(filter, rowData);
        }
    }
}
