package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
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
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Activator;

/**
 * @author carsten
 * 
 */
public class ServicesResource extends GridDataServerResource {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * constructor providing the freemarker template.
     */
    public ServicesResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:services.ftl");
    }

    @Override
    public void configureColumns(final ColumnsBuilder builder) {
        // @formatter:off CHECKSTYLE:OFF
        builder.
        addColumn("id").setWidth(0).
        addColumn("serviceName").sortDesc(1).setWidth(500).
        addColumn("implementingBundle").setWidth(240).
        addColumn("version").setWidth(80).
        addColumn("usingBundles").sortAsc(null).setWidth(400);
        // @formatter:on CHECKSTYLE:ON
    }

    @Override
    public void filterData() {
        ServiceReference[] services = getMatchingServices(getSkysailData().getFilter());
        GridData grid = getSkysailData();
        for (ServiceReference service : services) {
            RowData rowData = new RowData();
            Map<String, String> columnData = putColumnData(service);
            rowData.setColumnData(new ArrayList<Object>(columnData.values()));
            grid.addRowData(rowData);
        }
    }

    /**
     * @param filter
     * @return
     */
    private ServiceReference[] getMatchingServices(Filter filter) {
        ServiceReference[] services;
        try {
            if (filter instanceof LdapSearchFilter) {
                services = Activator.getContext().getServiceReferences(null, filter.toString());
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
            }
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Invalid Syntax for filter", e);
        }
        logger.debug("found {} matching services for request", services.length);
        return services;
    }

    /**
     * @param service
     * @return
     */
    private Map<String, String> putColumnData(ServiceReference service) {
        Map<String, String> columnData = new LinkedHashMap<String, String>();
        // for now, not too nice
        // to string gives something like
        // {org.eclipse.osgi.framework.console.CommandProvider}={service.ranking=2147483647,
        // service.id=2}
        // TODO Revisit
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

}
