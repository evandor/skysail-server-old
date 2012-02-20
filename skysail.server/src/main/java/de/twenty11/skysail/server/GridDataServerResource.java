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
import java.util.Map;

import de.twenty11.skysail.common.grids.ColumnDefinition;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.messages.GridData;

public abstract class GridDataServerResource extends SkysailServerResource<GridData> {

    public GridDataServerResource(GridData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
    }
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public GridData currentPageResults(int pageSize) {
        GridData grid = getSkysailData();
        int max = Math.min(grid.getSize(), (getCurrentPage() * pageSize));
        //for (int j = ((getCurrentPage() - 1) * pageSize); j < max; j++) {}
        for (int j = grid.getSize()-1; j > max; j--) {
            grid.removeRow(j);
        }
        for (int j = ((getCurrentPage() - 1) * pageSize); j >= 0; j--) {
            grid.removeRow(j);
        }
        return grid;
    }

    
    public void sort() {
        
        Map<String, ColumnDefinition> columnsInSortOrder = getSkysailData().getColumnsFromBuilder(true);

        String toggleColumn = getQuery().getFirstValue("toggleSorting", null);
        if (toggleColumn != null) {
            ColumnDefinition columnToToggle = columnsInSortOrder.get(toggleColumn);
            if (columnToToggle != null) {
                Integer sorting = columnToToggle.getSorting();
                if (sorting > 0) {
                    columnToToggle.sortAsc(sorting);
                    setSorting(toggleColumn + "=" + sorting);
                } else if (sorting == 0) {
                    
                } else {
                    columnToToggle.sortDesc(sorting);
                }
            }
        }
        
        for (String columnName : columnsInSortOrder.keySet()) {
            final ColumnDefinition colDef = columnsInSortOrder.get(columnName);
            if (colDef.getSorting() != null && colDef.getSorting() != 0) {
                Collections.sort(getSkysailData().getGridData(), new Comparator<RowData>() {
                    @Override
                    public int compare(final RowData o1, final RowData o2) {
                        int sig = 1;
                        if(colDef.getSorting() < 0) {
                            sig = -1;
                        }
                        return sig * o1.getColumnData().get(1).toString().compareTo(o2.getColumnData().get(1).toString());
                    }
                });
            }
        }

    }

}
