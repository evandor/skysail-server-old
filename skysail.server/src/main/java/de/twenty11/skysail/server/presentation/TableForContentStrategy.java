package de.twenty11.skysail.server.presentation;

import java.util.List;
import java.util.Map.Entry;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.grids.GridData;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.common.responses.SkysailResponse;

public class TableForContentStrategy extends AbstractHtmlCreatingStrategy {

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<table  class=\"table table-hover\">\n");
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            int i = 0;
            if (data != null) {
                for (Object object : data) {
                    i = handleDataElementsForTable(sb, i, object);
                }
            }
        } else if (skysailResponseAsObject instanceof GridData) {
            GridData data = (GridData) skysailResponseAsObject;
            List<RowData> rows = data.getRows();
            for (RowData rowData : rows) {
                handleDataElementsForRow(sb, rowData);
            }
        } else {
            handleDataElementsForList(sb, 1, skysailResponseAsObject);
        }
        sb.append("</table>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

    private int handleDataElementsForTable(StringBuilder sb, int i, Object object) {
        StringBuilder row = new StringBuilder("<tr>\n");
        i++;
        if (object instanceof Presentable) {
            Presentable presentable = (Presentable) object;
            for (Entry<String, Object> rowContent : presentable.getContent().entrySet()) {
                sb.append("<td>");
                sb.append(rowContent.getValue());
                sb.append("</td>");
            }
            sb.append("<td>").append(headerlink(presentable)).append("</td>");
        } else {

        }
        sb.append("</tr>");
        sb.append(row).append("\n");
        return i;
    }

    private void handleDataElementsForRow(StringBuilder sb, RowData rowData) {
        StringBuilder row = new StringBuilder("<tr>\n");
        // i++;

        for (String key : rowData.getCells().keySet()) {
            sb.append("<td>");
            sb.append(rowData.getCells().get(key));
            sb.append("</td>");
        }
        sb.append("<td>").append("there").append("</td>");

        sb.append("</tr>");
        sb.append(row).append("\n");
    }

}
