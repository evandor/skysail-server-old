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
//import java.util.Dictionary;
//import java.util.Enumeration;
//import java.util.List;
//
//import org.osgi.framework.Bundle;
//
//import de.twenty11.skysail.common.grids.ColumnsBuilder;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.common.messages.GridData;
//import de.twenty11.skysail.server.SkysailServerResource;
//import de.twenty11.skysail.server.osgi.bundles.internal.BundleUtils;
//
//public class BundleHeaderResource extends SkysailServerResource<GridData> {
//
//    public BundleHeaderResource() {
//        super(new GridData());
//        setTemplate("skysail.server.osgi.bundles:header.ftl");
//    }
//
//    public GridData getData() {
//        String bundleId = (String) getRequest().getAttributes().get(Constants.BUNDLE_ID);
//        Bundle bundle = BundleUtils.getInstance().getBundle(Long.parseLong(bundleId));
//        GridData grid = new GridData();
//        
//        Dictionary headers = bundle.getHeaders();
//        //String imported = (String)headers.get("Import-Package");
//        Enumeration keys = headers.keys();
//        while (keys.hasMoreElements()) {
//            Object nextElement = keys.nextElement();
//            RowData rowData = new RowData();
//            List<Object> columnData = new ArrayList<Object>();
//            columnData.add(nextElement.toString());
//            columnData.add((String)headers.get(nextElement));
//            rowData.setColumnData(columnData);
//            grid.addRowData(rowData);
//            
//        }
////        
//        return grid;
//    }
//
//    @Override
//    public void configureColumns(ColumnsBuilder builder) {
//        // TODO Auto-generated method stub
//        
//    }
//
//    
//
//    @Override
//    public void sort() {
//        // TODO Auto-generated method stub
//        
//    }
//   
//
//    @Override
//    public int handlePagination() {
//        // TODO Auto-generated method stub
//        return 0;
//    }
//
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
