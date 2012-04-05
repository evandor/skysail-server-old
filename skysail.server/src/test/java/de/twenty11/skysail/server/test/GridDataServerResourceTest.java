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

package de.twenty11.skysail.server.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restlet.Request;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.GridData;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.server.GridDataServerResource;

public class GridDataServerResourceTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private GridDataServerResource gdsr;

    @Before
    public void setUp() throws Exception {
        gdsr = new GridDataServerResource() {

            @Override
            public void configureColumns(final ColumnsBuilder builder) {
                // @formatter:off CHECKSTYLE:OFF
                builder.
                addColumn("id").setWidth(0).
                addColumn("serviceName").sortDesc(1).setWidth(500).
                addColumn("implementingBundle").setWidth(240).
                addColumn("version").setWidth(80).
                addColumn("usingBundles").sortAsc(null).setWidth(400);
                // @formatter:on CHECKSTYLE:ON
            }
            
            @Override
            public void buildGrid() {
                // @formatter:off CHECKSTYLE:OFF
                GridData grid = getSkysailData();
                setDummyData(grid, new String[] {"0",  "org.eclipse.osgi",                  "3.6.1.R36x_v20100806", "ACTIVE"});
                setDummyData(grid, new String[] {"1",  "ch.qos.logback.core",               "0.9.29",               "ACTIVE"});
                setDummyData(grid, new String[] {"5",  "skysail.common",                    "0.2.13.SNAPSHOT",      "ACTIVE"});
                setDummyData(grid, new String[] {"9",  "skysail.server",                    "0.1.3",                "ACTIVE"});
                setDummyData(grid, new String[] {"19", "skysail.server.servicedefinitions", "0.2.15.SNAPSHOT",      "ACTIVE"});
                setDummyData(grid, new String[] {"11", "skysail.server.serviceprovider",    "0.3.1",                "ACTIVE"});
                // @formatter:on CHECKSTYLE:ON
            }

            private void setDummyData(GridData grid, String[] a) {
                RowData rowData = new RowData(getSkysailData().getColumns());
                rowData.add(a[0]).add(a[1]).add(a[2]).add(a[3]);
//                List<Object> columnData = new ArrayList<Object>();
//                columnData.add(a[0]);columnData.add(a[1]);columnData.add(a[2]);columnData.add(a[3]);
//                rowData.setColumnData(columnData);
                grid.addRowData(null,rowData);
            }
        };
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testHandlePagination() {
        assertTrue(gdsr.handlePagination() == 15);
    }

    @Test
    public void testGetData() {
        GridData data = gdsr.getFilteredData();
        // TODO
        //assertTrue(data.getAvailableRowsCount() == 5);
        assertTrue(data.getColumns().size() == 5);
    }

    @Test
    public void testCurrentPageResults() {
        gdsr.getFilteredData();
        GridData currentPageResults = gdsr.currentPageResults(2);
        assertTrue(currentPageResults.getGridData().size() == 2);
    }

    /**
     * the search parameter s has only four parts (but there are five columns)
     */
    @Test(expected=IllegalArgumentException.class)
    public void testSortSortingParameterTooShort() {
        Request request = new Request();
        request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=0%7C0%7C0%7C0&pageSize=15&toggleSorting=1");
        gdsr.setRequest(request);
        gdsr.getFilteredData();
    }

    @Test
    public void testSortSymbolicName() {
        Request request = new Request();
        request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=0|1|0|0|0&pageSize=15");
        gdsr.setRequest(request);
        GridData data = gdsr.getFilteredData();
        assertTrue(data.getGridData().size() == 6);
        assertTrue(data.getGridData().get(0).getColumnData().get(1).equals("ch.qos.logback.core"));
        assertTrue(data.getGridData().get(1).getColumnData().get(1).equals("org.eclipse.osgi"));
        assertTrue(data.getGridData().get(2).getColumnData().get(1).equals("skysail.common"));
    }
    
    //@Test TODO
    public void testSortId() {
        Request request = new Request();
        request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=1|0|0|0|0&pageSize=15");
        gdsr.setRequest(request);
        GridData data = gdsr.getFilteredData();
        assertTrue(data.getGridData().get(0).getColumnData().get(0).equals(0));
        assertTrue(data.getGridData().get(1).getColumnData().get(0).equals(1));
        assertTrue(data.getGridData().get(2).getColumnData().get(0).equals(5));
    }


    @Test
    public void testSetResponseDetailsSkysailResponseOfGridData() {
        //fail("Not yet implemented");
    }

    @Test
    public void testGetFilter() {
        //fail("Not yet implemented");
    }

    @Test
    public void testSetSorting() {
        //fail("Not yet implemented");
    }

    @Test
    public void testGetPageSize() {
        //fail("Not yet implemented");
    }

    @Test
    public void testSetPageSize() {
        //fail("Not yet implemented");
    }

    @Test
    public void testGetCurrentPage() {
        //fail("Not yet implemented");
    }

    @Test
    public void testSetCurrentPage() {
        //fail("Not yet implemented");
    }

    @Test
    public void testSetTotalResults() {
        //fail("Not yet implemented");
    }

    @Test
    public void testGetTotalResults() {
        //fail("Not yet implemented");
    }

}
