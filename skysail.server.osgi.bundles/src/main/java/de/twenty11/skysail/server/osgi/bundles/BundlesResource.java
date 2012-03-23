package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.GridData;
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
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public void configureColumns(final ColumnsBuilder builder) {
        // @formatter:off
       builder.
       addColumn("id").setWidth(50).sortDesc(1).
       addColumn("symbolicName").setWidth(350).setWidth(100).
       addColumn("version").setWidth(100).
       addColumn("state").setWidth(50);
        // @formatter:on
    }

    @Override
    public void buildGrid() {
        List<Bundle> bundles = BundleUtils.getInstance().getBundles();
        Filter filter = getSkysailData().getFilter();
        for (Bundle bundle : bundles) {
            RowData rowData = new RowData(getSkysailData().getColumns());
            // @formatter:off
            rowData
                .add(bundle.getBundleId())
                .add(bundle.getSymbolicName())
                .add(bundle.getVersion())
                .add(BundleUtils.translateStatus(bundle.getState()));
            // @formatter:on

            // List<Object> columnData = new ArrayList<Object>();
            // columnData.add(bundle.getBundleId());
            // columnData.add(bundle.getSymbolicName());
            // columnData.add(bundle.getVersion());
            // columnData.add(BundleUtils.translateStatus(bundle.getState()));
            // //if (filter.match(columnData)) {
            // rowData.setColumnData(columnData);
            getSkysailData().addRowData(filter, rowData);
            // //}
        }
    }
}
