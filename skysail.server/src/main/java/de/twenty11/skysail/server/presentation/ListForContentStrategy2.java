package de.twenty11.skysail.server.presentation;

import java.net.URL;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class ListForContentStrategy2 extends AbstractHtmlCreatingStrategy {

    public ListForContentStrategy2(BundleContext bundleContext) {
        URL resource = bundleContext.getBundle().getResource("map.stg");
    }

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<div class=\"accordion\" id=\"accordion2\">\n");
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            int i = 0;
            if (data != null) {
                for (Object object : data) {
                    i = handleDataElementsForList2(sb, i, object);
                }
            }
        } else {
            handleDataElementsForList2(sb, 1, skysailResponseAsObject);
        }
        sb.append("</div>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

}
