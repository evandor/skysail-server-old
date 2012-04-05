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

import org.apache.felix.bundlerepository.Reason;
import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.apache.felix.bundlerepository.Resolver;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.InvalidSyntaxException;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.server.GridDataServerResource;
import de.twenty11.skysail.server.osgi.obr.internal.RepositoryAdminService;

public class InstallResource extends GridDataServerResource {

    public InstallResource() {
        setTemplate("skysail.server.osgi.obr:install.ftl");
    }

    @Override
    public void configureColumns(ColumnsBuilder builder) {
        builder.addColumn("result");
    }

    @Override
    public void buildGrid() {
        String bundle = (String) getRequest().getAttributes().get("bundle");
        RepositoryAdmin repAdmin = RepositoryAdminService.getInstance();
        Resolver resolver = repAdmin.resolver();
        Resource[] resource;
        try {
            resource = repAdmin.discoverResources("(symbolicname=" + bundle + ")");
            resolver.add(resource[0]);
            if (resolver.resolve()) {
                resolver.deploy(Resolver.NO_OPTIONAL_RESOURCES);
            } else {
                Reason[] reqs = resolver.getUnsatisfiedRequirements();
                for (int i = 0; i < reqs.length; i++) {
                    System.out.println("Unable to resolve: " + reqs[i]);
                }
            }
        } catch (InvalidSyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
