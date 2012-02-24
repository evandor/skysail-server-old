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
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.ColumnSortOrderComparator;
import de.twenty11.skysail.common.grids.ColumnDefinition;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;

/**
 * An abstract class dealing with common functionality for a skysail server
 * resource which is backed-up by a GridData object.
 * 
 * @author carsten
 * 
 */
public abstract class GridDataServerResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param data
     *            the skysail data backing up the resource.
     */
    public GridDataServerResource(final GridData data) {
        super(data);
    }

    @Override
    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GridData currentPageResults(final int pageSize) {
        GridData grid = getSkysailData();
        int max = Math.min(grid.getSize(), (getCurrentPage() * pageSize));
        // for (int j = ((getCurrentPage() - 1) * pageSize); j < max; j++) {}
        for (int j = grid.getSize() - 1; j > max; j--) {
            grid.removeRow(j);
        }
        for (int j = ((getCurrentPage() - 1) * pageSize); j >= 0; j--) {
            grid.removeRow(j);
        }
        return grid;
    }

    /**
     * @see de.twenty11.skysail.server.SkysailServerResource#sort()
     */
    public void sort() {

        Map<String, ColumnDefinition> columnsInSortOrder = getSkysailData().getColumnsFromBuilder(true);
        int maxSortValue = getSkysailData().getMaxSortValueFromBuilder();

        // check if a sorting instruction exists on the request and resort the
        // columns in that case.
        if (sortingRequested(columnsInSortOrder, maxSortValue)) {
            ColumnSortOrderComparator bvc = new ColumnSortOrderComparator(columnsInSortOrder);
            TreeMap<String, ColumnDefinition> result = new TreeMap<String, ColumnDefinition>(bvc);
            result.putAll(columnsInSortOrder);
            columnsInSortOrder = result;
        }

        // now sort the rows
        for (String columnName : columnsInSortOrder.keySet()) {
            final ColumnDefinition colDef = columnsInSortOrder.get(columnName);
            if (colDef.getSorting() != null && colDef.getSorting() != 0) {
                final int columnIndex = getSkysailData().getColumnId(colDef.getName());
                logger.info("sorting grid with column '{}' ({})", colDef.getName(), colDef.getSorting());
                sortRows(colDef, columnIndex);
            }
        }

    }

    /**
     * @param colDef
     *            the column which is used for sorting
     * @param columnIndex
     *            the index of that column
     */
    private void sortRows(final ColumnDefinition colDef, final int columnIndex) {
        Collections.sort(getSkysailData().getGridData(), new Comparator<RowData>() {
            @Override
            public int compare(final RowData o1, final RowData o2) {
                int sig = 1;
                if (colDef.getSorting() < 0) {
                    sig = -1;
                }
                return sig
                                * o1.getColumnData().get(columnIndex).toString()
                                                .compareTo(o2.getColumnData().get(columnIndex).toString());
            }
        });
    }

    /**
     * @param columnsInSortOrder
     *            the columns of the grid
     * @param maxSortValue
     * @return whether or not the sorting has been changed
     */
    private boolean sortingRequested(final Map<String, ColumnDefinition> columnsInSortOrder, int maxSortValue) {
        
        boolean toggled = false;
        Map<Integer, Integer> sortingMap = new TreeMap<Integer, Integer>(); 
        setSorting("");

        Integer columnIdToToggle = getColumnToToggle();
        ColumnDefinition columnToToggle = getColumnDefinitionToToggle(columnsInSortOrder, columnIdToToggle);
        String override = getQuery().getFirstValue("s", null);
        
        for (String currentColumnName : columnsInSortOrder.keySet()) {
            Integer sorting = columnsInSortOrder.get(currentColumnName).getSorting();
            if (override != null) {
                String[] sortValuesFromRequest = override.split("\\|");
                sorting = new Integer(sortValuesFromRequest[getSkysailData().getColumnId(currentColumnName)]);
                maxSortValue = Math.max(maxSortValue, Math.abs(sorting));
            }
            if (columnToToggle != null && currentColumnName.equals(columnToToggle.getName())) {
                toggled = true;
                if (sorting > 0) {
                    columnToToggle.sortAsc((maxSortValue + 1));
                    sortingMap.put(getSkysailData().getColumnId(currentColumnName), -(maxSortValue + 1));
                } else if (sorting == 0) {
                    columnToToggle.sortDesc((maxSortValue + 1));
                    sortingMap.put(getSkysailData().getColumnId(currentColumnName), (maxSortValue + 1));
                } else {
                    columnToToggle.sortDesc(0);
                    sortingMap.put(getSkysailData().getColumnId(currentColumnName), 0);
                }
            } else {
                sortingMap.put(getSkysailData().getColumnId(currentColumnName), sorting);
            }
        }

        if (sortingMap.size() > 0) {
            StringBuffer sb = new StringBuffer("s=");
            for (Integer position : sortingMap.keySet()) {
                Integer value = sortingMap.get(position);
                sb.append(value != null ? value + "|": 0 + "|");
            }
            setSorting(sb.toString().substring(0, sb.length()-1) + "&");
        }
        return toggled;
    }

    private ColumnDefinition getColumnDefinitionToToggle(Map<String, ColumnDefinition> columnsInSortOrder, Integer columnId) {
        if (columnId == null) {
            return null;
        }
        String columnNameToToggle = getSkysailData().getColumnName(columnId);
        return columnsInSortOrder.get(columnNameToToggle);
    }

    private Integer getColumnToToggle() {
        String toggleColumnAsString = getQuery().getFirstValue("toggleSorting", null);
        try {
            return new Integer(toggleColumnAsString);
        } catch (Exception e) {
            return null;
        }
 
    }

}
