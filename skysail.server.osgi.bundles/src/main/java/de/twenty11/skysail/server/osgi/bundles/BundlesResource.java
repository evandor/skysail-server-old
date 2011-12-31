package de.twenty11.skysail.server.osgi.bundles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.ColumnsBuilder;
import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class BundlesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundlesResource() {
        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public GridData getData() {

        // create the grid
        final GridData grid = new GridData();

        // pagination
        int pageSize = handlePagination();

        // @formatter:off
        // define the columns
        grid.setColumnsBuilder(new ColumnsBuilder(getQuery().getValuesMap()) {
            @Override
            public void configure() {
                addColumn("id").setWidth(50).
                addColumn("symbolicName").setWidth(250).sortDesc(1).setWidth(100).
                addColumn("version").setWidth(100).
                addColumn("state").setWidth(50);
            }
        });
        // @formatter:on

        // filtering
        Filter filter = grid.getFilter();

        
        GridData bundles = Bundles.getInstance().getBundles(grid, null);// filter);
        setTotalResults(bundles.getAvailableRows());
        return bundles;
    }

}
