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

package de.twenty11.skysail.server;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.grids.ColumnDefinition;
import de.twenty11.skysail.common.grids.ColumnSortOrderComparator;
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.GridData;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.internal.ConfigServiceProvider;
import de.twenty11.skysail.server.servicedefinitions.ConfigService;

/**
 * An class dealing with common functionality for a skysail server resource
 * which is backed-up by a GridData object.
 * 
 * The class is not abstract in order to let jackson deserialize json requests
 * more easily.
 * 
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform}
 * class and its main {@link Restlet} subclass where a single instance can
 * handle several calls concurrently, one instance of {@link ServerResource} is
 * created for each call handled and accessed by only one thread at a time.
 * 
 * @author carsten
 * 
 */
public class GridDataServerResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Filter filter;

    private String sortingRepresentation;

    private Integer currentPage = 1;

    private Integer pageSize;

    private int totalResults;

    /**
     * @param data
     *            the skysail data backing up the resource.
     */
    public GridDataServerResource() {
        super(new GridData());
    }

    public void buildGrid() {
        logger.error("you should implement a subclass of GridDataServerResource and overwrite method filterData");
    }

    public void configureColumns(ColumnsBuilder builder) {
        logger.error("you should implement a subclass of GridDataServerResource and overwrite method configureColumns");
    }

    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
    }

    /**
     * Implementors of this class have to provide skysailData which will be used
     * to create a restlet representation. Which type of representation (json,
     * xml, ...) will be returned depends on the request details.
     * 
     * @return Type extending SkysailData
     * 
     */
    public final GridData getFilteredData() {

        Map<String, String> params = new HashMap<String, String>();
        if (getQuery() != null) {
            params = getQuery().getValuesMap();
        }

        // define the columns for the result (for grids and assign to grid)
        ColumnsBuilder columnsBuilder = new ColumnsBuilder(params) {
            @Override
            public void configure() {
                configureColumns(this);
            }
        };
        if (getSkysailData() instanceof GridData) {
            ((GridData) getSkysailData()).setColumnsBuilder(columnsBuilder);
        }

        // get the data, applying the current filter
        buildGrid();

        // sort the results
        sort();

        // handle Page size and pagination
        int pageSize = handlePagination();
        setPageSize(pageSize);
        // how many results do we have (all pages)
        setTotalResults(getSkysailData().getSize());

        // get results for current page
        return currentPageResults(pageSize);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public GridData currentPageResults(final int pageSize) {
        GridData grid = getSkysailData();
        int max = Math.min(grid.getSize(), (getCurrentPage() * pageSize));
        // for (int j = ((getCurrentPage() - 1) * pageSize); j < max; j++) {}
        for (int j = grid.getSize() - 1; j >= max; j--) {
            grid.removeRow(j);
        }
        for (int j = ((getCurrentPage() - 1) * pageSize) - 1; j >= 0; j--) {
            grid.removeRow(j);
        }
        return grid;
    }

    /**
     * Sorting the grid.
     * 
     * The steps involved are:
     * 
     * 1. As a default get the columns by which we should sort (in an ascending
     * order by sort-weight) 2. Adjust that column sorting in case we have
     * instructions to do so (via request) 3. Sort the rows of the grid,
     * starting from the column with the lowest sort-weight
     * 
     * @see de.twenty11.skysail.server.SkysailServerResource#sort()
     * 
     *      // TODO whole implementation of sorting completely ugly, needs
     *      serious refactoring, specifically "sortingRequested"
     */
    public void sort() {

        // 1.) get the default sorting for the grid by asking for the pre-sorted
        // columns (ascending by sort weight). The columns with the highest sort-order
    	// come last. maxSortWeight is the maximum absolute sorting value of all columns.
        List<ColumnDefinition> columnsInSortOrder = getSkysailData().getColumnsInSortOrder();
        int maxSortWeight = getSkysailData().getMaxSortValueFromBuilder();

        // 2.) check if a sorting instruction exists on the request and re-sort
        // the columns in that case.
        if (calculateNewSorting(columnsInSortOrder, maxSortWeight)) {
        	Collections.sort(columnsInSortOrder,new ColumnSortOrderComparator());
        }

        // 3.) now sort the rows, starting with the column with the lowest
        // sort-weight
        for (ColumnDefinition colDef  : columnsInSortOrder) {
            if (colDef.getSorting() != null && colDef.getSorting() != 0) {
                final int columnIndex = getSkysailData().getColumnId(colDef.getName());
                logger.info("sorting grid with column '{}' ({})", colDef.getName(), colDef.getSorting());
                sortRows(colDef, columnIndex);
            }
        }
    }

    /**
     * // TODO this needs some refinement (comparing object.tostring() right now)
     * @param colDef
     *            the column which is used for sorting
     * @param columnIndex
     *            the index of that column
     */
    private void sortRows(final ColumnDefinition colDef, final int columnIndex) {
        Collections.sort(getSkysailData().getGrid(), new Comparator<RowData>() {
            @Override
            public int compare(final RowData o1, final RowData o2) {
                List<Object> columnData1 = o1.getColumnData();
                List<Object> columnData2 = o2.getColumnData();
                
                // make this null-safe for the columnData object
                if (columnData1 == null ^ columnData2 == null) {
                    return (columnData1 == null) ? -1 : 1;
                }
                if (columnData1 == null && columnData2 == null) {
                    return 0;
                }
                
                // make this null-safe for the actual entry
                Object object1 = columnData1.get(columnIndex);
                Object object2 = columnData2.get(columnIndex);
                if (object1 == null ^ object2 == null) {
                    return (object1 == null) ? -1 : 1;
                }
                if (object1 == null && object2 == null) {
                    return 0;
                }
                // now, finally, compare
                int sig = 1;
                if (colDef.getSorting() < 0) {
                    sig = -1;
                }
                return sig * object1.toString().compareTo(object2.toString());
            }
        });
    }

    /**
     * @param columnsInSortOrder
     *            the columns of the grid
     * @param maxSortValue
     * @return whether or not the sorting has been changed
     */
    private boolean calculateNewSorting(final List<ColumnDefinition> columnsInSortOrder, int maxSortValue) {

        boolean runReSort = false;
        Map<Integer, Integer> sortingMap = new TreeMap<Integer, Integer>();
        setSorting("");
        
        // is there any column the sorting of which should be toggled?
        ColumnDefinition columnToToggle = getColumnDefinitionToToggle(columnsInSortOrder, getColumnToToggle());
        String override = getQuery() != null ? getQuery().getFirstValue("s", null) : null;
        runReSort = validate(override, getSkysailData().getColumns().size());

        for (ColumnDefinition currentColumnName : columnsInSortOrder) {
            Integer sorting = currentColumnName.getSorting();
            if (override != null) {
                sorting = setSortingAsByRequest(columnsInSortOrder, sortingMap,
						override, currentColumnName);
                maxSortValue = Math.max(maxSortValue, Math.abs(sorting));
            }
            runReSort = toggleColumnsSorting(maxSortValue, runReSort,
					sortingMap, columnToToggle, currentColumnName, sorting);
        }

        calcAndSetSortingString(sortingMap);
        return runReSort;
    }

	private boolean toggleColumnsSorting(int maxSortValue, boolean runReSort,
			Map<Integer, Integer> sortingMap, ColumnDefinition columnToToggle,
			ColumnDefinition currentColumnName, Integer sorting) {
		if (columnToToggle != null && currentColumnName.equals(columnToToggle.getName())) {
		    runReSort = true;
		    if (sorting > 0) {
		        columnToToggle.sortAsc((maxSortValue + 1));
		        sortingMap.put(getSkysailData().getColumnId(currentColumnName.getName()), -(maxSortValue + 1));
		    } else if (sorting == 0) {
		        columnToToggle.sortDesc((maxSortValue + 1));
		        sortingMap.put(getSkysailData().getColumnId(currentColumnName.getName()), (maxSortValue + 1));
		    } else {
		        columnToToggle.sortDesc(0);
		        sortingMap.put(getSkysailData().getColumnId(currentColumnName.getName()), 0);
		    }
		} else {
		    sortingMap.put(getSkysailData().getColumnId(currentColumnName.getName()), sorting);
		}
		return runReSort;
	}

	private Integer setSortingAsByRequest(
			final List<ColumnDefinition> columnsInSortOrder,
			Map<Integer, Integer> sortingMap, String override,
			ColumnDefinition currentColumnName) {
		Integer sorting;
		String[] sortValuesFromRequest = override.split("\\|");
		sorting = new Integer(sortValuesFromRequest[getSkysailData().getColumnId(currentColumnName.getName())]);
		sortingMap.put(getSkysailData().getColumnId(currentColumnName.getName()), sorting);
		if (sorting > 0) {
		    getColumnDefinitionToToggle(columnsInSortOrder, getSkysailData().getColumnId(currentColumnName.getName()))
		                    .sortDesc(sorting);
		} else {
		    getColumnDefinitionToToggle(columnsInSortOrder, getSkysailData().getColumnId(currentColumnName.getName()))
		                    .sortAsc(sorting);
		}
		return sorting;
	}

	private void calcAndSetSortingString(Map<Integer, Integer> sortingMap) {
		if (sortingMap.size() > 0) {
            StringBuffer sb = new StringBuffer("s=");
            for (Integer position : sortingMap.keySet()) {
                Integer value = sortingMap.get(position);
                sb.append(value != null ? value + "|" : 0 + "|");
            }
            setSorting(sb.toString().substring(0, sb.length() - 1) + "&");
        }
	}

    /**
     * checks the sorting parameter and throws unchecked exception is not valid
     * 
     * @param override
     * @param size
     * @return
     */
    private boolean validate(String override, int size) {
        if (override == null || override.trim().equals("")) {
            return false;
        }
        String[] sortValuesFromRequest = override.split("\\|");
        if (sortValuesFromRequest.length != size) {
            throw new IllegalArgumentException("parameter 's' for sorting has " + sortValuesFromRequest.length
                            + "parts, but should have " + size);
        }
        for (String part : sortValuesFromRequest) {
            try {
                Integer.valueOf(part);
            } catch (Exception e) {
                throw new IllegalArgumentException("could not parse '" + part + "' of sorting parameter " + override
                                + " as Integer");
            }
        }
        return true;
    }

    private ColumnDefinition getColumnDefinitionToToggle(List<ColumnDefinition> columnsInSortOrder,
                    Integer columnId) {
        if (columnId == null) {
            return null;
        }
        String columnNameToToggle = getSkysailData().getColumnName(columnId);
        //return columnsInSortOrder.get(columnNameToToggle);
        for (ColumnDefinition columnDefinition : columnsInSortOrder) {
			if (columnDefinition.getName().equals(columnNameToToggle)) {
				return columnDefinition;
			}
		}
        return null;
    }

    private Integer getColumnToToggle() {
        String toggleColumnAsString = getQuery() != null ? getQuery().getFirstValue("toggleSorting", null) : null;
        try {
            return new Integer(toggleColumnAsString);
        } catch (Exception e) {
            return null;
        }

    }

    public void setResponseDetails(SkysailResponse<GridData> response) {
        response.setMessage(getMessage());
        response.setTotalResults(getTotalResults());
        response.setPage(getCurrentPage());
        response.setPageSize(getPageSize());
        //response.setOrigRequest(getRequest().getOriginalRef().toUrl());
        response.setRequest(getRequest().getOriginalRef().toString());
        response.setParent(getParent());
        response.setContextPath("/rest/");
        response.setFilter(getFilter() != null ? getFilter().toString() : "");
        response.setSortingRepresentation(getSorting());
        if (getQuery() != null && getQuery().getNames().contains("debug")) {
            response.setDebug(true);
        }
    }

    protected int doHandlePagination(String configIdentifier, int defaultSize) {
        int pageSize = 20;
        String firstValue = getQuery() != null ? getQuery().getFirstValue("page", "1") : "1";
        int page = Integer.parseInt(firstValue);
        setCurrentPage(page);

        ConfigService configService = ConfigServiceProvider.getConfigService();
        String pageSizeFromProperties = null;
        if (configService != null) {
            pageSizeFromProperties = configService.getString(configIdentifier);
        }
        if (pageSizeFromProperties != null && pageSizeFromProperties.trim().length() > 0) {
            pageSize = Integer.parseInt(pageSizeFromProperties);
        } else {
            pageSize = defaultSize;
        }
        String pageSizeParam = getQuery() != null ? getQuery().getFirstValue("pageSize", null) : null;
        if (pageSizeParam != null) {
            pageSize = Integer.parseInt(pageSizeParam);
        }

        return pageSize;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public void setSorting(String str) {
        sortingRepresentation = str;
    }

    private String getSorting() {
        return sortingRepresentation != null ? sortingRepresentation : "";
    }

    protected Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    protected Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    protected void setTotalResults(int length) {
        this.totalResults = length;
    }

    public int getTotalResults() {
        return totalResults;
    }

}
