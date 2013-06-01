package de.twenty11.skysail.server.presentation;

import java.util.List;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class ListForContentStrategy extends AbstractHtmlCreatingStrategy {

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<div class=\"accordion\" id=\"accordion2\">\n");
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            int i = 0;
            if (data != null) {
                for (Object object : data) {
                    i = handleDataElementsForList(sb, i, object);
                }
            }
        } else {
            handleDataElementsForList(sb, 1, skysailResponseAsObject);
        }
        sb.append("</div>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

}
