package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.SkysailServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class BundleResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    //private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundleResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:bundle.ftl");
    }

    @Override
    public void setColumns(GridData data) {
        data.setColumnsBuilder(new ColumnsBuilder(getQuery().getValuesMap()) {
            @Override
            public void configure() {
                addColumn("Key").addColumn("Value");
            }
        });
    }

    @Override
    public List<?> getFilteredData() {
        String bundleId = (String) getRequest().getAttributes().get(Constants.BUNDLE_ID);
        Bundle bundle = Bundles.getInstance().getBundle(Long.parseLong(bundleId));
        List<Bundle> result = new ArrayList<Bundle>();
        result.add(bundle);
        return result;
    }

    @Override
    public int handlePagination() {
        // TODO Auto-generated method stub
        return 15;
    }

    @Override
    public GridData currentPageResults(List<?> filterResults, int pageSize) {
        GridData grid = getSkysailData();
        Bundle bundle = (Bundle)filterResults.get(0);
        addRow(grid, "id", Long.toString(bundle.getBundleId()));
        addRow(grid, "state", Bundles.translateStatus(bundle.getState()));
        addRow(grid, "lastModified", Long.toString(bundle.getLastModified()));
        addRow(grid, "version", bundle.getVersion().toString());
        addRow(grid, "location", bundle.getLocation());
        return grid;
    }

    private void addRow(GridData grid, String key, Object value) {
        RowData rowData = new RowData();
        List<Object> columnData = new ArrayList<Object>();
        columnData.add(key);
        columnData.add(value);
        rowData.setColumnData(columnData );
        grid.addRowData(rowData);
    }

}
