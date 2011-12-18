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

package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.osgi.framework.internal.core.BundleHost;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.RowData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;
import de.twenty11.skysail.server.osgi.bundles.internal.BundlesUrlMapper;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class BundleHeaderResource extends SkysailServerResource<GridData> {

    public BundleHeaderResource() {
        super("Bundle Header");
        setTemplate("skysail.server.osgi.bundles:header.ftl");
    }

    @Override
    public GridData getData() {
        String bundleId = (String) getRequest().getAttributes().get(OsgiBundlesConstants.BUNDLE_ID);
        Bundle bundle = Bundles.getInstance().getBundle(Long.parseLong(bundleId));
        GridData grid = new GridData();
        
        Dictionary headers = bundle.getHeaders();
        //String imported = (String)headers.get("Import-Package");
        Enumeration keys = headers.keys();
        while (keys.hasMoreElements()) {
            Object nextElement = keys.nextElement();
            RowData rowData = new RowData();
            List<Object> columnData = new ArrayList<Object>();
            columnData.add(nextElement.toString());
            columnData.add((String)headers.get(nextElement));
            rowData.setColumnData(columnData);
            grid.addRowData(rowData);
            
        }
        
        return grid;
    }

}
