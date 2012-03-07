///**
// *  Copyright 2011 Carsten Gräf
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// * 
// */
//
//package de.twenty11.skysail.server.osgi.bundles;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.eclipse.osgi.framework.internal.core.AbstractBundle;
//import org.eclipse.osgi.service.resolver.BundleDescription;
//import org.eclipse.osgi.service.resolver.ExportPackageDescription;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
//
//import de.twenty11.skysail.common.grids.ColumnsBuilder;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.common.messages.GridData;
//import de.twenty11.skysail.server.SkysailServerResource;
//import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
//import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;
//
//public class ExportedPackagesResource extends SkysailServerResource<GridData> {
//
//    public ExportedPackagesResource() {
//        super(new GridData());
//
//        setTemplate("skysail.server.osgi.bundles:imported.ftl");
//    }
//
////    @Override
//    public GridData getData() {
//        String bundleId = (String) getRequest().getAttributes().get(Constants.BUNDLE_ID);
//        GridData grid = new GridData();
//
//        // TODO equinox case only!
//        AbstractBundle bundle =  (AbstractBundle)Bundles.getInstance().getBundle(Long.parseLong(bundleId));
//        setMessage("Exported Packages for bundle " + bundle.getSymbolicName());
//        
//        BundleContext context = Activator.getContext();
//        ServiceReference packageAdminRef = context.getServiceReference("org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$
//        if (packageAdminRef != null) {
//            BundleDescription desc = bundle.getBundleDescription();
//            if (desc != null) {
//                ExportPackageDescription[] exports = desc.getExportPackages();
//                if (exports != null && exports.length > 0) {
//
//                    for (int i = 0; i < exports.length; i++) {
//                        RowData rowData = new RowData();
//                        List<Object> columnData = new ArrayList<Object>();
//                        columnData.add(exports[i].getName());
//                        columnData.add(exports[i].getVersion());
//                        columnData.add(exports[i].getSupplier());
//                        columnData.add(exports[i].getAttributes() != null ? exports[i].getAttributes() : "-");
//                        columnData.add(exports[i].getExporter() != null ? exports[i].getExporter() : "-");
//                        rowData.setColumnData(columnData);
//                        grid.addRowData(rowData);
//                    }
//                }
//            }
//        }
//        return grid;
//    }
//
//    @Override
//    public void configureColumns(ColumnsBuilder builder) {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public void sort() {
//        // TODO Auto-generated method stub
//        
//    }
//   
//
////    @Override
////    public List<Object> getFilteredData() {
////        // TODO Auto-generated method stub
////        return null;
////    }
//
//   
//
//    @Override
//    public int handlePagination() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
////    @Override
////    public GridData currentPageResults(List<?> filterResults, int pageSize) {
////        // TODO Auto-generated method stub
////        return null;
////    }
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
