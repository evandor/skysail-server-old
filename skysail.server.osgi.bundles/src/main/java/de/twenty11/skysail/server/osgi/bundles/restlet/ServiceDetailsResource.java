package de.twenty11.skysail.server.osgi.bundles.restlet;
//package de.twenty11.skysail.server.osgi.bundles;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.osgi.framework.Bundle;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.common.grids.ColumnsBuilder;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.common.messages.GridData;
//import de.twenty11.skysail.server.SkysailServerResource;
//import de.twenty11.skysail.server.osgi.bundles.internal.BundleUtils;
//
//public class ServiceDetailsResource extends SkysailServerResource<GridData> {
//
//    /** slf4j based logger implementation */
//    Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    public ServiceDetailsResource() {
//        super(new GridData());
//        setTemplate("skysail.server.osgi.bundles:service.ftl");
//    }
//
////    public GridData getData() {
////        
////        String serviceId = (String) getRequest().getAttributes().get(Constants.SERVICE_ID);
////        try {
////            ServiceReference[] serviceReferences = Activator.getContext().getServiceReferences(null, null);
////            ServiceReference serviceForId;
//////            for (ServiceReference serviceReference : serviceReferences) {
//////                if (serviceReference)
//////            }
////            GridData form = new GridData();
////
////            TextFieldData tfd = new TextFieldData("symbolicName", "");
////            form.addField(tfd);
////
////            return form;
////        } catch (InvalidSyntaxException e) {
////            throw new RuntimeException("Invalid Syntax for filter", e);
////        }
////        
////        
////    }
//
//    @Override
//    public void configureColumns(ColumnsBuilder builder) {
//        builder.addColumn("Key").addColumn("Value");
//    }
//
////    @Override
////    public List<?> getFilteredData() {
////        String bundleId = (String) getRequest().getAttributes().get(Constants.SERVICE_ID);
////        Bundle bundle = BundleUtils.getInstance().getBundle(Long.parseLong(bundleId));
////        List<Bundle> result = new ArrayList<Bundle>();
////        result.add(bundle);
////        return result;
////    }
//
//    @Override
//    public int handlePagination() {
//        return 15;
//    }
//    
//    @Override
//    public void sort() {
//        // TODO Auto-generated method stub
//        
//    }
//    /**
//     * @see de.twenty11.skysail.server.SkysailServerResource#currentPageResults(java.util.List, int)
//     */
////    @Override
////    public GridData currentPageResults(final List<?> filterResults, final int pageSize) {
////        GridData grid = getSkysailData();
////        Bundle bundle = (Bundle)filterResults.get(0);
////        addRow(grid, "id", Long.toString(bundle.getBundleId()));
////        addRow(grid, "state", BundleUtils.translateStatus(bundle.getState()));
////        addRow(grid, "lastModified", Long.toString(bundle.getLastModified()));
////        addRow(grid, "version", bundle.getVersion().toString());
////        addRow(grid, "location", bundle.getLocation());
////        return grid;
////    }
//
//    private void addRow(GridData grid, String key, Object value) {
//        RowData rowData = new RowData();
//        List<Object> columnData = new ArrayList<Object>();
//        columnData.add(key);
//        columnData.add(value);
//        rowData.setColumnData(columnData );
//        grid.addRowData(rowData);
//    }
//
//    @Override
//    public void filterData() {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public GridData currentPageResults(int pageSize) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//}
