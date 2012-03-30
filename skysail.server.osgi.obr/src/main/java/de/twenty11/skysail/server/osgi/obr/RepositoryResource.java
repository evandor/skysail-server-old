/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.osgi.obr;

import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.InvalidSyntaxException;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.GridData;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.obr.internal.RepositoryAdminService;

public class RepositoryResource extends GridDataServerResource {

    public RepositoryResource() {
        super(new GridData());
        setTemplate("skysail.server.osgi.obr:repository.ftl");
    }
    
    @Override
    public void configureColumns(ColumnsBuilder builder) {
        builder.addColumn("bundle").sortDesc(2).setWidth(150);
        builder.addColumn("version").sortDesc(1).setWidth(100);
        builder.addColumn("url").setWidth(900);
    }
    
    @Override
    public void buildGrid() {
        RepositoryAdmin repAdmin = RepositoryAdminService.getInstance();
        //String repositoryName = (String) getRequest().getAttributes().get("repositoryName");
        try {
            String filter = null;
            Resource[] resources = repAdmin.discoverResources(filter);
            for (Resource resource : resources) {
                RowData row = new RowData(getSkysailData().getColumns());
                row.add(resource.getSymbolicName());
                row.add(resource.getVersion());
                row.add(resource.getURI());
                getSkysailData().addRowData(null, row);
            }
            
        } catch (InvalidSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
