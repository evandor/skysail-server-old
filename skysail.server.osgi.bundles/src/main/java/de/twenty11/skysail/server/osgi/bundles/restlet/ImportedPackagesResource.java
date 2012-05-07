package de.twenty11.skysail.server.osgi.bundles.restlet;
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
//import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
//import org.eclipse.osgi.service.resolver.StateHelper;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
//
//import de.twenty11.skysail.common.grids.ColumnsBuilder;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.common.messages.GridData;
//import de.twenty11.skysail.server.SkysailServerResource;
//import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
//import de.twenty11.skysail.server.osgi.bundles.internal.BundleUtils;
//
//public class ImportedPackagesResource extends SkysailServerResource<GridData> {
//
//    public ImportedPackagesResource() {
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
//        AbstractBundle bundle =  (AbstractBundle)BundleUtils.getInstance().getBundle(Long.parseLong(bundleId));
//        
//        setMessage("Imported Packages for bundle " + bundle.getSymbolicName());
//        BundleContext context = Activator.getContext();
//        ServiceReference packageAdminRef = context.getServiceReference("org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$
//        if (packageAdminRef != null) {
//            BundleDescription desc = bundle.getBundleDescription();
//            if (desc != null) {
//                ArrayList fragmentsImportPackages = getFragmentImports(desc.getFragments());
//
//                ImportPackageSpecification[] importPackages;
//                if (fragmentsImportPackages.size() > 0) {
//                    ImportPackageSpecification[] directImportPackages = desc.getImportPackages();
//                    importPackages = new ImportPackageSpecification[directImportPackages.length + fragmentsImportPackages.size()];
//
//                    for (int i = 0; i < directImportPackages.length; i++) {
//                        importPackages[i] = directImportPackages[i];
//                    }
//
//                    int offset = directImportPackages.length;
//                    for (int i = 0; i < fragmentsImportPackages.size(); i++) {
//                        importPackages[offset + i] = (ImportPackageSpecification) fragmentsImportPackages.get(i);
//                    }
//                } else {
//                    importPackages = desc.getImportPackages();
//                }
//
//                // Get all resolved imports
//                ExportPackageDescription[] imports = null;
//                imports = desc.getContainingState().getStateHelper().getVisiblePackages(desc, StateHelper.VISIBLE_INCLUDE_EE_PACKAGES | StateHelper.VISIBLE_INCLUDE_ALL_HOST_WIRES);
//
//                // Get the unresolved optional and dynamic imports
////                ArrayList unresolvedImports = new ArrayList();
////
////                for (int i = 0; i < importPackages.length; i++) {
////                    if (importPackages[i].getDirective(Constants.RESOLUTION_DIRECTIVE).equals(ImportPackageSpecification.RESOLUTION_OPTIONAL)) {
////                        if (importPackages[i].getSupplier() == null) {
////                            unresolvedImports.add(importPackages[i]);
////                        }
////                    } else if (importPackages[i].getDirective(org.osgi.framework.Constants.RESOLUTION_DIRECTIVE).equals(ImportPackageSpecification.RESOLUTION_DYNAMIC)) {
////                        boolean isResolvable = false;
////
////                        // Check if the dynamic import can be resolved by any of the wired imports, 
////                        // and if not - add it to the list of unresolved imports
////                        for (int j = 0; j < imports.length; j++) {
////                            if (importPackages[i].isSatisfiedBy(imports[j])) {
////                                isResolvable = true;
////                            }
////                        }
////
////                        if (isResolvable == false) {
////                            unresolvedImports.add(importPackages[i]);
////                        }
////                    }
////                }
//                
//                for (int i = 0; i < importPackages.length; i++) {
//                    RowData rowData = new RowData();
//                    List<Object> columnData = new ArrayList<Object>();
//                    columnData.add(importPackages[i].getName());
//                    columnData.add(importPackages[i].getVersionRange());
//                    rowData.setColumnData(columnData);
//                    grid.addRowData(rowData);
//                }
//
////                if (desc.isResolved() && (unresolvedImports.isEmpty() == false)) {
////                    for (int i = 0; i < unresolvedImports.size(); i++) {
////                        ImportPackageSpecification importPackage = (ImportPackageSpecification) unresolvedImports.get(i);
////                        RowData rowData = new RowData();
////                        List<Object> columnData = new ArrayList<Object>();
////                        columnData.add(importPackage.getName());
////                        columnData.add(importPackage.getVersionRange());
////                    }
////                }
//
//            }
//        }
//        return grid;
//    }
//    
//    @Override
//    public void sort() {
//        // TODO Auto-generated method stub
//        
//    }
//
//    private ArrayList getFragmentImports(BundleDescription[] fragments) {
//        ArrayList fragmentsImportPackages = new ArrayList();
//        for (int i = 0; i < fragments.length; i++) {
//            ImportPackageSpecification[] fragmentImports = fragments[i].getImportPackages();
//            for (int j = 0; j < fragmentImports.length; j++) {
//                fragmentsImportPackages.add(fragmentImports[j]);
//            }
//        }
//        return fragmentsImportPackages;
//    }
//
//    @Override
//    public void configureColumns(ColumnsBuilder builder) {
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
//}
