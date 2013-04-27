package de.twenty11.skysail.server.converter;

import java.awt.Color;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.PresentableHeader;
import de.twenty11.skysail.common.responses.SkysailResponse;

public abstract class AbstractHtmlCreatingStrategy implements HtmlCreatingStrategy {

    private InputStream accordionGroupTemplateResource = this.getClass().getResourceAsStream("accordionGroup.template");
    final String accordionGroupTemplate = convertStreamToString(accordionGroupTemplateResource);

    public static String convertStreamToString(java.io.InputStream is) {
        @SuppressWarnings("resource")
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public abstract String createHtml(String page, Object skysailResponseAsObject,
            SkysailResponse<List<?>> skysailResponse);

    protected int handleDataElementsForList(StringBuilder sb, int i, Object object) {
        String accordionGroup = accordionGroupTemplate;
        i++;
        if (object instanceof Presentable) {
            Presentable presentable = (Presentable) object;
            accordionGroup = accordionGroup.replace("${headerText}", presentable.getHeader().getText());
            accordionGroup = accordionGroup.replace("${headerImage}", presentable.getHeader().getImage());
            accordionGroup = accordionGroup.replace("${headerCategoryIcon}", headerCategory(presentable));
            accordionGroup = accordionGroup.replace("${headerLink}", headerlink(presentable));
            accordionGroup = accordionGroup.replace("${hlink}", link(presentable));
            accordionGroup = accordionGroup.replace("${inner}", getInner(presentable));
        } else {
            accordionGroup = accordionGroup.replace("${headerText}", "Entry #" + i);
            accordionGroup = accordionGroup.replace("${headerImage}", "icon-list-alt");
            accordionGroup = accordionGroup.replace("${headerCategoryIcon}", "");
            accordionGroup = accordionGroup.replace("${headerLink}", "");
            accordionGroup = accordionGroup.replace("${inner}", object.toString());
        }
        accordionGroup = accordionGroup.replace("${index}", String.valueOf(i));
        sb.append(accordionGroup).append("\n");
        return i;
    }

    private String link(Presentable presentable) {
        if (presentable.getHeader().getLink() == null) {
            return "";
        }
        return presentable.getHeader().getLink();
    }

    protected String headerlink(Presentable presentable) {
        if (presentable.getHeader().getLink() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='").append(presentable.getHeader().getLink()).append("' id='")
                .append(presentable.getHeader().getText()).append("'>&nbsp;<i class='icon-zoom-in'></i>&nbsp;</a>\n");
        return sb.toString();
    }

    private String headerCategory(Presentable presentable) {
        if ((presentable.getHeader().getCategoryText() == null || presentable.getHeader().getCategoryText().equals(""))
                && presentable.getHeader().getCategoryColor() == null) {
            return "";
        }
        String text = presentable.getHeader().getCategoryText();
        Color color = presentable.getHeader().getCategoryColor();
        String hex = "#ffffff";
        if (color != null) {
            hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        }
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<button class=\"btn btn-mini\" type=\"button\" style=\"background-image: linear-gradient(to bottom, "
                        + hex + ", " + hex + ");\">").append(text == null ? "&nbsp;" : text).append("</button>\n");
        return sb.toString();
    }

    private String getInner(Presentable presentable) {
        StringBuilder sb = new StringBuilder("<table class=\"table table-hover\" style='width:90%'>\n");
        for (Entry<String, Object> row : presentable.getContent().entrySet()) {
            sb.append("<tr>\n");
            sb.append("<th style='width:200px;'>").append(row.getKey()).append("</th>");

            if (row.getValue() instanceof List) {
                List<?> list = (List<?>) row.getValue();
                StringBuilder valueSb = new StringBuilder();
                // sb.append("<td style='width:600px;'>");
                for (Object object : list) {
                    if (object instanceof Presentable) {
                        PresentableHeader header = ((Presentable) object).getHeader();
                        valueSb.append("<a href='" + header.getLink() + "'>").append(header.getText()).append("</a>");
                    } else {
                        valueSb.append(object.toString()).append(", ");
                    }
                }
                // sb.append("</td>\n");
                sb.append("<td style='width:600px;'>").append(valueSb.toString()).append("</td>\n");
            } else if (row.getValue() instanceof Presentable) {
                printPresentableHeader(sb, row);
            } else {
                sb.append("<td style='width:600px;'>").append(row.getValue()).append("</td>\n");
            }
            sb.append("</tr>\n");
        }
        return sb.append("</table>\n").toString();
    }

    private void printPresentableHeader(StringBuilder sb, Entry<String, Object> row) {
        PresentableHeader header = ((Presentable) row.getValue()).getHeader();
        sb.append("<td style='width:600px;'><a href='" + header.getLink() + "'>").append(header.getText())
                .append("</a></td>\n");
    }

}
