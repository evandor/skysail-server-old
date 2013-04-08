package de.twenty11.skysail.server.converter;

import de.twenty11.skysail.common.responses.SkysailResponse;

import java.util.List;

public class IFrameForContentStrategy extends AbstractHtmlCreatingStrategy {

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<iframe src='");
        sb.append("asGraph/d3Simple");
        sb.append("' style='width:100%;height:600px;' frameBorder=0>\n");
        sb.append("</iframe>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }


}
