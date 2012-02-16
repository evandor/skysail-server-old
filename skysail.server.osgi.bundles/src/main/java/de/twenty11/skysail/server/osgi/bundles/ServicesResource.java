package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.filters.LdapSearchFilter;
import de.twenty11.skysail.common.grids.ColumnDefinition;
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.SkysailServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Activator;

public class ServicesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServicesResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:services.ftl");
    }

    @Override
    public void setColumns(GridData gridData) {
        // @formatter:off
        gridData.setColumnsBuilder(new ColumnsBuilder(getQuery().getValuesMap()) {
            @Override
            public void configure() {
                addColumn("id").setWidth(0).
                addColumn("serviceName").sortDesc(1).setWidth(230).
                addColumn("implementingBundle").setWidth(200).
                addColumn("version").setWidth(100).
                addColumn("usingBundles").setWidth(330);
            }
        });
        // @formatter:on
    }

    @Override
    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
    }

    @Override
    public List<Object> getFilteredData() {

        Filter filter = getSkysailData().getFilter();
        Object[] services;

        try {
            if (filter instanceof LdapSearchFilter) {
                services = Activator.getContext().getServiceReferences(null, filter.toString());
                return Arrays.asList(services);
            } else {
                // need to filter "manually"
                services = Activator.getContext().getServiceReferences(null, null);
                List<ServiceReference> filteredReferences = new ArrayList<ServiceReference>();
                if (services != null) {
                    for (int j = 0; j < services.length; j++) {
                        ServiceReference service = (ServiceReference) services[j];
                        Map<String, String> columnData = putColumnData(service);
                        if (filter.match(columnData)) {
                            filteredReferences.add(service);
                        }
                    }
                }
                services = filteredReferences.toArray(new ServiceReference[filteredReferences.size()]);
                return Arrays.asList(services);
            }
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Invalid Syntax for filter", e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GridData currentPageResults(List<?> filterResults, int pageSize) {
        GridData grid = getSkysailData();
        int max = Math.min(filterResults.size(), (getCurrentPage() * pageSize));
        for (int j = ((getCurrentPage() - 1) * pageSize); j < max; j++) {
            ServiceReference service = (ServiceReference) filterResults.get(j);
            RowData rowData = new RowData();
            Map<String, String> columnData = putColumnData(service);
            rowData.setColumnData(new ArrayList(columnData.values()));
            grid.addRowData(rowData);
        }
        return grid;
    }

    private Map<String, String> putColumnData(ServiceReference service) {
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
        return columnData;
    }

    public void sort(GridData data, List<?> filterResults) {
        Map<String, ColumnDefinition> columnsInSortOrder = data.getColumnsFromBuilder(true);
        for (String columnName : columnsInSortOrder.keySet()) {
            ColumnDefinition colDef = columnsInSortOrder.get(columnName);
            if (colDef.getSorting() != 0) {
                Collections.sort(filterResults, new Comparator() {
                    @Override
                    public int compare(Object o1, Object o2) {
                        // TODO Auto-generated method stub
                        return 0;
                    }
                });
            }
        }

    }

}
