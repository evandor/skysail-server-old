package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.ColumnsBuilder;
import de.twenty11.skysail.common.RowData;
import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.SkysailServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class BundlesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundlesResource() {
        super(new GridData());

        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public void setColumns(GridData data) {
        // @formatter:off
        data.setColumnsBuilder(new ColumnsBuilder(getQuery().getValuesMap()) {
            @Override
            public void configure() {
                addColumn("id").setWidth(50).
                addColumn("symbolicName").setWidth(250).sortDesc(1).setWidth(100).
                addColumn("version").setWidth(100).
                addColumn("state").setWidth(50);
            }
        });
        // @formatter:on
    }

    @Override
    public List<?> getFilteredData() {
        // Filter filter = getSkysailData().getFilter();
         List<?> bundles = Bundles.getInstance().getBundles();
         return bundles;
    }

    @Override
    public int handlePagination() {
        return doHandlePagination("", 15);
    }

    @Override
    public GridData currentPageResults(List<?> filteredResults, int pageSize) {
        for (Object object : filteredResults) {
            AbstractBundle bundle = (AbstractBundle) object;
            
            RowData rowData = new RowData();
            
            List<Object> columnData = new ArrayList<Object>();
            columnData.add(bundle.getBundleId());
            columnData.add(bundle.getSymbolicName());
            columnData.add(bundle.getVersion());
            columnData.add(Bundles.translateStatus(bundle.getState()));
            rowData.setColumnData(columnData );
            getSkysailData().addRowData(rowData );
        }
        return getSkysailData();
    }

}
