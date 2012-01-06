package de.twenty11.skysail.server.osgi.bundles;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.SkysailServerResource;

public class PackagesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private static final String[] fields = { "Package", "version", "Exporting Bundle", "Importing Bundles" };

    public PackagesResource() {
        super(new GridData());

        setTemplate("skysail.server.osgi.bundles:packages.ftl");
    }

//    @Override
    public GridData getData() {
//        GridInfo fieldsList = SkysailUtils.createFieldList(fields);
        GridData grid = new GridData();
//        
//        BundleContext context = Activator.getContext();
//        Bundle bundle = null;
//        ServiceReference packageAdminRef = context.getServiceReference("org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$
//        if (packageAdminRef != null) {
//            PackageAdmin packageAdmin = (PackageAdmin) context.getService(packageAdminRef);
//            if (packageAdmin != null) {
//                ExportedPackage[] packages = null;
//                packages = packageAdmin.getExportedPackages(bundle);
//                if (packages != null) {
//                    for (int i = 0; i < packages.length; i++) {
//                        RowData rowData = new RowData();
//                        List<Object> columnData = new ArrayList<Object>();
//                        ExportedPackage pkg = packages[i];
//                        
//                        columnData.add(pkg.getName());
//                        columnData.add(pkg.getVersion());
//                        columnData.add(pkg.getExportingBundle().getSymbolicName());
//                        Bundle[] importingBundles = pkg.getImportingBundles();
//                        StringBuffer sb = new StringBuffer();
//                        for (Bundle importingBundle : importingBundles) {
//                            sb.append(importingBundle.getSymbolicName()).
//                                append(" (").append(importingBundle.getVersion()).append(" )").
//                                append(" [").append(importingBundle.getBundleId()).append("]").
//                                append(";");
//                        }
//                        columnData.add(sb.toString());
//                        
//                        rowData.setColumnData(columnData);
//                        grid.addRowData(rowData);
//                    }
//                }
//            }
//        }
        return grid;
    }

    @Override
    public void setColumns(GridData data) {
        // TODO Auto-generated method stub
        
    }

  

    @Override
    public List<Object> getFilteredData() {
        // TODO Auto-generated method stub
        return null;
    }

   

    @Override
    public int handlePagination() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public GridData currentPageResults(List<?> filterResults, int pageSize) {
        // TODO Auto-generated method stub
        return null;
    }

}
