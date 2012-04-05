package de.twenty11.skysail.server.osgi.obr;

import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.RepositoryAdmin;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.obr.internal.RepositoryAdminService;

/**
 * @author carsten
 * 
 */
public class RepositoriesResource extends GridDataServerResource {

    /**
     * constructor providing the freemarker template.
     */
    public RepositoriesResource() {
        setTemplate("skysail.server.osgi.obr:repositories.ftl");
    }

    @Override
    public void configureColumns(final ColumnsBuilder builder) {
        builder.addColumn("repositoryName").setWidth(300);
        builder.addColumn("repositoryLocation").setWidth(240);
    }

    @Override
    public void buildGrid() {
        RepositoryAdmin repAdmin = RepositoryAdminService.getInstance();
        Repository[] repositories = repAdmin.listRepositories();
        for (Repository repository : repositories) {
            RowData rowData = new RowData(getSkysailData().getColumns());
            rowData.add(repository.getName(), "unknown");
            rowData.add(repository.getURI());
            getSkysailData().addRowData(null, rowData);
        }
    }


}
