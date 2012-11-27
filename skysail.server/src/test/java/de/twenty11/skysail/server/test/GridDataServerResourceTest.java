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
//package de.twenty11.skysail.server.test;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.restlet.Request;
//
//import de.twenty11.skysail.common.grids.ColumnsBuilder;
//import de.twenty11.skysail.common.grids.GridData;
//import de.twenty11.skysail.common.grids.RowData;
//import de.twenty11.skysail.server.restlet.GridDataServerResource;
//
///**
// * Testing GridDataServerResource class.
// * @author carsten
// *
// */
//public class GridDataServerResourceTest {
//
//	/** initialized in setup for testing. */
//	private GridDataServerResource gdsr;
//
//	@Before
//	public void setUp() throws Exception {
//		ColumnsBuilder builder = new ColumnsBuilder() {
//			
//			@Override
//			public void configure() {
//				addColumn("col0").setWidth(0);
//				addColumn("col1").sortDesc(1).setWidth(500);
//				addColumn("col2").setWidth(240);
//				addColumn("col3").setWidth(80);
//				addColumn("col4").sortAsc(null).setWidth(400);
//			}
//		};
//		gdsr = new GridDataServerResource(builder) {
//
//			@Override
//			public void buildGrid() {
//				GridData grid = getSkysailData();
//				setDummyData(grid, new String[] { "6", "e", "1.2.3", "ACTIVE" });
//				setDummyData(grid, new String[] { "5", "d", "10.11.12",
//						"ACTIVE" });
//				setDummyData(grid, new String[] { "4", "c", "4.5.6", "ACTIVE" });
//				setDummyData(grid, new String[] { "3", "b", "13.14.15",
//						"ACTIVE" });
//				setDummyData(grid, new String[] { "2", "a", "7.8.9", "ACTIVE" });
//				setDummyData(grid, new String[] { "1", "a", "16.17.18",
//						"ACTIVE" });
//			}
//
//			private void setDummyData(GridData grid, String[] a) {
//				RowData rowData = new RowData(getSkysailData().getColumns());
//				rowData.add(a[0]).add(a[1]).add(a[2]).add(a[3]);
//				grid.addRowData(rowData);
//			}
//		};
//	}
//
//	@Test
//	public void testHandlePagination() {
//		assertEquals(15,gdsr.handlePagination());
//	}
//
//	@Test
//	public void testGetData() {
//		GridData data = gdsr.getFilteredData();
//		assertEquals(5, data.getColumns().getAsList().size());
//	}
//
//	@Test
//	public void testCurrentPageResults() {
//		gdsr.getFilteredData();
//		GridData currentPageResults = gdsr.currentPageResults(2);
//		assertEquals(2,currentPageResults.getRows().size());
//	}
//
//	/**
//	 * the search parameter s has only four parts (but there are five columns)
//	 */
//	@Test(expected = IllegalArgumentException.class)
//	public void testSortSortingParameterTooShort() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=0|0|0|0&pageSize=15&toggleSorting=1");
//		gdsr.setRequest(request);
//		gdsr.getFilteredData();
//	}
//
//	/**
//	 * tests the sorting defined in column definition, i.e. desc by col1
//	 */
//	@Test
//	public void testPresorting() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/");
//		gdsr.setRequest(request);
//		int row = 0;
//		GridData data = gdsr.getFilteredData();
//		assertEquals("s=0|1|0|0|0&",data.getSortingRepresentation());
//		assertEquals(6, data.getRows().size());
//		assertEquals("a", data.getGridElement(row++, 1));
//		assertEquals("a", data.getGridElement(row++, 1));
//		assertEquals("b", data.getGridElement(row++, 1));
//		assertEquals("c", data.getGridElement(row++, 1));
//		assertEquals("d", data.getGridElement(row++, 1));
//		assertEquals("e", data.getGridElement(row++, 1));
//	}
//
//	/**
//	 * tests the sorting defined in column definition, i.e. desc by col1, plus
//	 * toggling of col0.
//	 */
//	@Test
//	public void testPresortingPlusToggling() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?toggleSorting=0");
//		gdsr.setRequest(request);
//		int row = 0;
//		GridData data = gdsr.getFilteredData();
//		assertEquals("s=2|1|0|0|0&",data.getSortingRepresentation());
//		assertEquals(6, data.getRows().size());
//		assertEquals("a", data.getGridElement(row++, 1));
//		assertEquals("a", data.getGridElement(row++, 1));
//		assertEquals("b", data.getGridElement(row++, 1));
//		assertEquals("c", data.getGridElement(row++, 1));
//		assertEquals("d", data.getGridElement(row++, 1));
//		assertEquals("e", data.getGridElement(row++, 1));
//	}
//
//	@Test
//	public void testSortSymbolicName() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=0|1|0|0|0&pageSize=15");
//		gdsr.setRequest(request);
//		GridData data = gdsr.getFilteredData();
//		assertEquals(data.getSortingRepresentation(), "s=0|1|0|0|0&");
//		assertEquals(data.getRows().size(), 6);
//		assertEquals(data.getGridElement(0, 1), "a");
//		assertEquals(data.getGridElement(1, 1), "a");
//		assertEquals(data.getGridElement(2, 1), "b");
//	}
//
//	@Test
//	public void testToggleSymbolicName() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=1|0|0|0|0&pageSize=15&toggleSorting=1");
//		gdsr.setRequest(request);
//		GridData data = gdsr.getFilteredData();
//		assertEquals("s=1|-2|0|0|0&",data.getSortingRepresentation());
//		assertEquals(6,data.getRows().size());
//		assertEquals("e",data.getGridElement(0, 1));
//		assertEquals("d",data.getGridElement(1, 1));
//		assertEquals("c",data.getGridElement(2, 1));
//		assertEquals("6",data.getGridElement(0, 0));
//		assertEquals("5",data.getGridElement(1, 0));
//		assertEquals("4",data.getGridElement(2, 0));
//	}
//
//	// @Test TODO
//	public void testSortId() {
//		Request request = new Request();
//		request.setResourceRef("http://localhost:8099/rest/osgi/bundles/?s=1|0|0|0|0&pageSize=15");
//		gdsr.setRequest(request);
//		GridData data = gdsr.getFilteredData();
//		assertEquals(data.getGridElement(0, 0), 0);
//		assertEquals(data.getGridElement(1, 0), 1);
//		assertEquals(data.getGridElement(2, 0), 5);
//	}
//
//	@Test
//	public void testSetResponseDetailsSkysailResponseOfGridData() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetFilter() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetSorting() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPageSize() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetPageSize() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetCurrentPage() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetCurrentPage() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testSetTotalResults() {
//		// fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetTotalResults() {
//		// fail("Not yet implemented");
//	}
//
//}
