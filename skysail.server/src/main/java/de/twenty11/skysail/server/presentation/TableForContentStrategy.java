package de.twenty11.skysail.server.presentation;

import java.util.List;

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

        sb.append("</tr>");
        sb.append(row).append("\n");
        return i;
    }



}
