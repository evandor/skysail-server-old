package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.ColumnsBuilder;
import de.twenty11.skysail.common.RowData;
import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class ServicesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServicesResource() {
        setTemplate("skysail.server.osgi.bundles:services.ftl");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.twenty11.skysail.server.restletosgi.SkysailServerResource#getData()
     * 
     * @see
     * org.eclipse.osgi.framework.internal.core.FrameworkCommandProvider._services
     * (CommandInterpreter)
     */
    @Override
    public GridData getData() {

        // create the grid
        GridData grid = new GridData();

        // get the filter
        Filter filter = getFilterFromQuery();

        // @formatter:off
        // define the columns
        grid.setColumnsBuilder(new ColumnsBuilder(filter) {
            @Override
            public void configure() {
                addColumn("id").setWidth(0).
                addColumn("serviceName").sortDesc(1).setWidth(100).
                addColumn("implementingBundle").
                addColumn("version").
                addColumn("usingBundles");
            }
        });
        // @formatter:on

        ServiceReference[] services;
        try {
            services = Activator.getContext().getServiceReferences(null, null);
            if (services != null) {
                int size = services.length;
                setTotalResults(size);
                if (size > 0) {
                    int count = 0;
                    for (int j = 0; j < size; j++) {
                        if (count < 20) {
                            ServiceReference service = services[j];

                            RowData rowData = new RowData();
                            Map<String, String> columnData = new LinkedHashMap<String, String>();

                            // for now, not too nice
                            // to string gives something like
                            // {org.eclipse.osgi.framework.console.CommandProvider}={service.ranking=2147483647,
                            // service.id=2}
                            String[] serviceSplit = service.toString().split("\\}=\\{");
                            String serviceId = "";
                            String[] values = serviceSplit[1].replace("}", "").replace("{", "").split(",");
                            for (String keyValueString : values) {
                                String[] keyValuePair = keyValueString.split("=");
                                if (keyValuePair[0].trim().equals("service.id")) {
                                    serviceId = keyValuePair[1].trim();
                                }
                            }
                            columnData.put("serviceId", serviceId);

                            String serviceName = serviceSplit[0].replace("}", "").replace("{", "");
                            columnData.put("serviceName", serviceName);
                            columnData.put("implementingBundle", service.getBundle().getSymbolicName());
                            columnData.put("version", service.getBundle().getVersion().toString());
                            Bundle[] users = service.getUsingBundles();
                            StringBuffer sb = new StringBuffer();
                            if (users != null) {
                                for (int k = 0; k < users.length; k++) {
                                    sb.append(users[k]).append(";");
                                }
                            }
                            columnData.put("usingBundles", sb.toString());
                            // apply filter to decide whether to include the
                            // entry
                            if (filter.match(columnData)) {
                                rowData.setColumnData(new ArrayList(columnData.values()));
                                grid.addRowData(rowData);
                            }

                        }
                        count++;
                    }
                    setTotalResults(count);
                }
            }
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Invalid Syntax for filter", e);
        }
        return grid;
    }

}
