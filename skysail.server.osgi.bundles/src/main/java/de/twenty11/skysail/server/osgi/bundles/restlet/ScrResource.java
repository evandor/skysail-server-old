package de.twenty11.skysail.server.osgi.bundles.restlet;
//package de.twenty11.skysail.server.osgi.bundles;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Vector;
//
//import org.eclipse.equinox.internal.ds.SCRUtil;
//import org.eclipse.equinox.internal.ds.SCRCommandProvider.ComponentRef;
//import org.eclipse.equinox.internal.ds.model.ServiceComponent;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.osgi.framework.ServiceReference;
//import org.osgi.service.packageadmin.ExportedPackage;
//import org.osgi.service.packageadmin.PackageAdmin;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.common.RowData;
//import de.twenty11.skysail.common.messages.GridData;
//import de.twenty11.skysail.common.messages.GridInfo;
//import de.twenty11.skysail.server.osgi.SkysailUtils;
//import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
//import de.twenty11.skysail.server.restletosgi.SkysailServerResource;
//
//public class ScrResource extends SkysailServerResource<GridData> {
//
//    /** slf4j based logger implementation */
//    Logger logger = LoggerFactory.getLogger(this.getClass());
//    
//    private static final String[] fields = { "ID", "state", "Component Name", "Located in" };
//
//    public ScrResource() {
//        super("Service Component Runtime");
//        setTemplate("skysail.server.osgi.bundles:scr.ftl");
//    }
//
//    @Override
//    public GridData getData() {
//        GridInfo fieldsList = SkysailUtils.createFieldList(fields);
//        GridData grid = new GridData(fieldsList.getColumns());
//        
//        BundleContext context = Activator.getContext();
//        Bundle[] bundles = context.getBundles();
//        
//        for (int j = 0; j < bundles.length; j++) {
//            Vector components = (Vector) scrManager.bundleToServiceComponents.get(allBundles[j]);
//            if (components != null) {
//                for (int i = 0; i < components.size(); i++) {
//                    ServiceComponent sc = (ServiceComponent) components.elementAt(i);
//                    ComponentRef aRef = new ComponentRef(allBundles[j].getBundleId(), sc.name);
//                    ComponentRef ref = (ComponentRef) componentRefsIDs.get(aRef);
//                    if (ref == null) {
//                        ref = aRef;
//                        ref.id = generateID();
//                        componentRefsIDs.put(ref, ref);
//                    }
//
//                    if (completeInfo) {
//                        intp.print(ref.id + ""); //$NON-NLS-1$
//                        printComponentDetails(intp, sc);
//                    } else {
//                        ////print short info
//                        intp.print("" + ref.id); //$NON-NLS-1$
//                        intp.print("\t" + SCRUtil.getStateStringRepresentation(sc.getState())); //$NON-NLS-1$
//                        intp.print("\t\t" + sc.name); //$NON-NLS-1$
//                        intp.println("\t\t\t" + getBundleRepresentationName(allBundles[j]) + "(bid=" + allBundles[j].getBundleId() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//                    }
//                }
//            }
//        }
//        
//        
//        
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
//        return grid;
//    }
//
//}
