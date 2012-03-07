package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.SkysailServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class PackagesResource extends GridDataServerResource {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public PackagesResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:packages.ftl");
    }

    @Override
    public void configureColumns(ColumnsBuilder builder) {
        // @formatter:off
        builder.addColumn("Package").sortDesc(1).setWidth(250).
        addColumn("Exporting Bundle").setWidth(300).
        addColumn("Version").
        addColumn("Importing Bundles");
        // @formatter:on
    }

    @Override
    public void filterData() {
        GridData data = getSkysailData();
        BundleContext context = Activator.getContext();
        Bundle bundle = null;

        ServiceReference packageAdminRef = context.getServiceReference("org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$
        if (packageAdminRef != null) {
            PackageAdmin packageAdmin = (PackageAdmin) context.getService(packageAdminRef);
            if (packageAdmin != null) {
                ExportedPackage[] packages = null;
                packages = packageAdmin.getExportedPackages(bundle);
                if (packages != null) {
                    for (int i = 0; i < packages.length; i++) {
                        RowData rowData = new RowData();
                        List<Object> columnData = new ArrayList<Object>();
                        ExportedPackage pkg = packages[i];

                        columnData.add(pkg.getName());
                        columnData.add(pkg.getExportingBundle().getSymbolicName());
                        columnData.add(pkg.getVersion());
                        Bundle[] importingBundles = pkg.getImportingBundles();
                        StringBuffer sb = new StringBuffer();
                        for (Bundle importingBundle : importingBundles) {
                            sb.append(importingBundle.getSymbolicName()).append(" (")
                                            .append(importingBundle.getVersion()).append(" )").append(" [")
                                            .append(importingBundle.getBundleId()).append("]").append(";");
                        }
                        columnData.add(sb.toString());

                        rowData.setColumnData(columnData);
                        data.addRowData(rowData);
                    }
                }
            }
        }

    }

}
