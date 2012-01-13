package de.twenty11.skysail.server.osgi.bundles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.ColumnsBuilder;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.SkysailServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class PackagesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public PackagesResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.bundles:packages.ftl");
    }

    // @Override
    public GridData getData() {
        // GridInfo fieldsList = SkysailUtils.createFieldList(fields);
        GridData grid = new GridData();
        //
        // BundleContext context = Activator.getContext();
        // Bundle bundle = null;
        //        ServiceReference packageAdminRef = context.getServiceReference("org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$
        // if (packageAdminRef != null) {
        // PackageAdmin packageAdmin = (PackageAdmin)
        // context.getService(packageAdminRef);
        // if (packageAdmin != null) {
        // ExportedPackage[] packages = null;
        // packages = packageAdmin.getExportedPackages(bundle);
        // if (packages != null) {
        // for (int i = 0; i < packages.length; i++) {
        // RowData rowData = new RowData();
        // List<Object> columnData = new ArrayList<Object>();
        // ExportedPackage pkg = packages[i];
        //
        // columnData.add(pkg.getName());
        // columnData.add(pkg.getVersion());
        // columnData.add(pkg.getExportingBundle().getSymbolicName());
        // Bundle[] importingBundles = pkg.getImportingBundles();
        // StringBuffer sb = new StringBuffer();
        // for (Bundle importingBundle : importingBundles) {
        // sb.append(importingBundle.getSymbolicName()).
        // append(" (").append(importingBundle.getVersion()).append(" )").
        // append(" [").append(importingBundle.getBundleId()).append("]").
        // append(";");
        // }
        // columnData.add(sb.toString());
        //
        // rowData.setColumnData(columnData);
        // grid.addRowData(rowData);
        // }
        // }
        // }
        // }
        return grid;
    }

    @Override
    public void setColumns(GridData data) {
        // @formatter:off
        data.setColumnsBuilder(new ColumnsBuilder(getQuery().getValuesMap()) {
            @Override
            public void configure() {
                addColumn("Package").
                addColumn("Version").
                addColumn("Exporting Bundle").
                addColumn("Importing Bundles");
            }
        });
        // @formatter:on
    }

    @Override
    public List<?> getFilteredData() {
        // get the package admin service
        ServiceReference packageAdminRef = Bundles.getInstance().getServiceReference(
                        "org.osgi.service.packageadmin.PackageAdmin"); //$NON-NLS-1$

        if (packageAdminRef != null) {
            PackageAdmin packageAdmin = (PackageAdmin) Bundles.getInstance().getService(packageAdminRef);
            if (packageAdmin != null) {
                ExportedPackage[] packages = null;
                Bundle bundle = null;
                packages = packageAdmin.getExportedPackages(bundle);
                if (packages != null) {
                    return Arrays.asList(packages);
                }
            }
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public int handlePagination() {
        return 15;
    }

    @Override
    public GridData currentPageResults(List<?> filteredResults, int pageSize) {
        GridData grid = getSkysailData();

        return grid;
    }

}
