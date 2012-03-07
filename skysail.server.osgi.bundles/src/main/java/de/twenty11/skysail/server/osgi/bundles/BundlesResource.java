package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class BundlesResource extends GridDataServerResource {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundlesResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public void configureColumns(ColumnsBuilder builder) {
        // @formatter:off
       builder.
       addColumn("id").setWidth(50).sortDesc(1).
       addColumn("symbolicName").setWidth(350).setWidth(100).
       addColumn("version").setWidth(100).
       addColumn("state").setWidth(50);
        // @formatter:on
    }

    @Override
    public void filterData() {
        List<Bundle> bundles = Bundles.getInstance().getBundles();
        GridData data = getSkysailData();
        for (Bundle bundle : bundles) {
            RowData rowData = new RowData();
            List<Object> columnData = new ArrayList<Object>();
            columnData.add(bundle.getBundleId());
            columnData.add(bundle.getSymbolicName());
            columnData.add(bundle.getVersion());
            columnData.add(Bundles.translateStatus(bundle.getState()));
            rowData.setColumnData(columnData);
            getSkysailData().addRowData(rowData);
        }
    }
}
