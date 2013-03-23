package de.twenty11.skysail.server.converter;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;
import org.restlet.routing.Route;
import org.restlet.routing.TemplateRoute;
import org.restlet.util.RouteList;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.PresentableHeader;
import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.commands.Command;
import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.common.navigation.LinkedPage;
import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.internal.Configuration.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.restlet.SkysailServerResource2;

public class Json2BootstrapConverter extends ConverterHelper {

    private InputStream bootstrapTemplateResource = this.getClass().getResourceAsStream("bootstrap.template");
    private final String rootTemplate = convertStreamToString(bootstrapTemplateResource);

    private InputStream accordionGroupTemplateResource = this.getClass().getResourceAsStream("accordionGroup.template");
    private final String accordionGroupTemplate = convertStreamToString(accordionGroupTemplateResource);

    private InputStream d3SimpleGraphTemplateResource = this.getClass().getResourceAsStream("d3SimpleGraph.template");
    private final String d3SimpleGraphTemplate = convertStreamToString(d3SimpleGraphTemplateResource);

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_JSON);

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
            result = 1.0F;
        } else {
            result = 0.5F;
        }
        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        float result = -1.0F;

        if (source instanceof JacksonRepresentation<?>) {
            result = 1.0F;
        } else if ((target != null) && JacksonRepresentation.class.isAssignableFrom(target)) {
            result = 1.0F;
        } else if (VARIANT_JSON.isCompatible(source)) {
            result = 0.8F;
        }

        return result;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_JSON.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, JacksonRepresentation.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;
        if (source != null) {
            result = addVariant(result, VARIANT_JSON);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        Representation representation;
        try {
            representation = new StringRepresentation(jsonToHtml((SkysailResponse) source, resource));
        } catch (Exception e) {
            representation = new StringRepresentation(jsonToHtml(new FailureResponse(e), resource));
        }
        representation.setMediaType(MediaType.TEXT_HTML);
        return representation;
    }

    public List<Breadcrumb> getBreadcrumbList(Resource resource) {

        List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();
        breadcrumbs.add(new Breadcrumb("/", null, "Home"));

        Reference reference = resource.getReference();
        SkysailApplication application = (SkysailApplication) resource.getApplication();
        RouteList routes = application.getRoutes();

        Reference rootRef = resource.getRootRef();
        if (!(resource.getApplication() instanceof DefaultSkysailApplication)) {
            breadcrumbs.add(new Breadcrumb(rootRef.toString(), null, resource.getApplication().getName()));
        }

        List<String> segments = reference.getSegments();
        String path = "/";
        for (int i = 1; i < segments.size(); i++) {
            path = path + segments.get(i);
            for (Route route : routes) {
                if (route instanceof TemplateRoute) {
                    TemplateRoute tr = (TemplateRoute) route;
                    String pattern = tr.getTemplate().getPattern();
                    if (pattern.equals(path)) {
                        breadcrumbs.add(new Breadcrumb("/" + resource.getApplication().getName() + path, null, path
                                .replace("/", "")));
                        break;
                    }
                }
            }

        }
        return breadcrumbs;
    }

    private String jsonToHtml(SkysailResponse<List<?>> skysailResponse, Resource resource) {

        PresentationStyle style = evalPresentationStyle(resource);

        String page = rootTemplate;
        // template = template.replace("${originalJson}", json);
        long executionTimeInNanos = skysailResponse.getExecutionTime();
        float performance = new Long(1000000000) / executionTimeInNanos;
        page = page.replace("${performance}", String.format("%s", performance));
        page = page.replace("${serverLoad}", String.format("%s", skysailResponse.getServerLoad()));
        page = page.replace("${result}", calcResult(skysailResponse));
        page = page.replace("${message}", skysailResponse.getMessage() == null ? "no message available"
                : skysailResponse.getMessage());
        page = page.replace("${linkedPages}", linkedPages(resource));
        page = page.replace("${commands}", commands(resource));
        page = page.replace("${presentations}", presentations());
        page = page.replace("${filterExpression}", getFilter());
        page = page.replace("${history}", getHistory());

        // TODO revisit
        Object skysailResponseAsObject = skysailResponse.getData();
        if (skysailResponseAsObject != null) {
            if (style.equals(PresentationStyle.LIST)) {
                page = createListForContent(page, skysailResponseAsObject);
            } else if (style.equals(PresentationStyle.TABLE)) {
                page = createTableForContent(page, skysailResponseAsObject);
            } else if (style.equals(PresentationStyle.EDIT)) {
                page = createFormForContent(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.D3_SIMPLE_GRAPH)) {
                page = createD3SimpleGraphForContent(skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.IFRAME)) {
                page = createIFrameForContent(page, skysailResponseAsObject, skysailResponse);
            }
        } else {
            if (skysailResponse instanceof ConstraintViolationsResponse) {
                page = createFormForContent(page, skysailResponseAsObject, skysailResponse);
            } else {
                page = page.replace("${content}", "");
            }
        }

        StringBuilder breadcrumb = getBreadcrumbHtml(resource);
        page = page.replace("${breadcrumb}", breadcrumb.toString());

        String stacktrace = "";
        if (skysailResponse instanceof FailureResponse) {
            Exception exception = ((FailureResponse<?>) skysailResponse).getException();
            if (exception != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                exception.printStackTrace(pw);
                stacktrace = "<pre>" + sw.toString() + "</pre>";
            }
        }
        page = page.replace("${stacktrace}", stacktrace);
        return page;
    }

    private PresentationStyle evalPresentationStyle(Resource resource) {
        PresentationStyle style = PresentationStyle.LIST;
        if (resource.getClass().isAnnotationPresent(Presentation.class)) {
            Presentation annotation = resource.getClass().getAnnotation(Presentation.class);
            style = annotation.preferred();
        }
        return style;
    }

    private String createListForContent(String page, Object skysailResponseAsObject) {
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

    private String createTableForContent(String page, Object skysailResponseAsObject) {
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

    private String createIFrameForContent(String page, Object skysailResponseAsObject,
            SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<iframe src='");
        sb.append("asGraph/d3Simple");
        sb.append("' style='width:100%;height:600px;' frameBorder=0>\n");
        sb.append("</iframe>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

    private String createD3SimpleGraphForContent(Object skysailResponseAsObject,
            SkysailResponse<List<?>> skysailResponse) {

        String template = d3SimpleGraphTemplate;
        StringBuilder links = new StringBuilder();

        List<Map<String, String>> data = (List<Map<String, String>>) skysailResponse.getData();
        boolean found = false;
        for (Map<String, String> object : data) {
            found = true;
            links.append("\n{source: '");
            links.append(object.get("source"));
            links.append("', target: '");
            links.append(object.get("target"));
            links.append("', type: 'licensing'},");
        }
        if (found) {
            links = links.delete(links.length() - 1, links.length());
        }
        // {source: "Microsoft", target: "Amazon", type: "licensing"},
        // {source: "Microsoft", target: "HTC", type: "licensing"},
        // {source: "Apple", target: "Samsung", type: "suit"},
        // {source: "Kodak", target: "RIM", type: "suit"},
        // {source: "Nokia", target: "Qualcomm", type: "suit"}
        //
        template = template.replace("${links}", links.toString());
        return template;
    }

    private String createFormForContent(String page, Object response, SkysailResponse<?> skysailResponse) {

        Set<ConstraintViolation> violations = null;
        Map<String, ConstraintViolation<?>> violationsMap = new HashMap<String, ConstraintViolation<?>>();

        String action = ".";
        if (skysailResponse instanceof ConstraintViolationsResponse) {
            ConstraintViolationsResponse cvr = (ConstraintViolationsResponse) skysailResponse;
            violations = cvr.getViolations();
            for (ConstraintViolation<?> violation : violations) {
                if (violation.getPropertyPath() != null) {
                    violationsMap.put(violation.getPropertyPath().toString(), violation);
                }
            }
        } else if (skysailResponse instanceof FormResponse) {
            FormResponse formResponse = (FormResponse) skysailResponse;
            action = formResponse.getTarget();
        }

        StringBuilder sb = new StringBuilder("<form class='form-horizontal' action='" + action + "' method='POST'>\n");

        java.lang.reflect.Field[] fields = response.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            Field formField = field.getAnnotation(Field.class);
            if (formField == null) {
                continue;
            }

            String value = "";
            try {
                // Field f = response.getClass().getDeclaredField("stuffIWant"); //NoSuchFieldException
                field.setAccessible(true);
                Object object = field.get(response);
                if (object instanceof String) {
                    value = (String) object;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String id = field.getName();
            String help = "";
            String cssClass = "control-group";
            if (violationsMap.containsKey(id)) {
                // Object object = constraintViolations.
                id = "inputError";
                cssClass = "control-group error";
                help = "<span class='help-inline'>" + violationsMap.get(field.getName()).getMessage() + "</span>";
            }

            sb.append("<div class='" + cssClass + "'>\n");
            sb.append("  <label class='control-label' for='" + id + "'>").append(field.getName()).append("</label>\n");
            sb.append("  <div class='controls'>\n");
            sb.append("<input type='text' id='" + id + "' name='" + field.getName() + "' placeholder='' value='")
                    .append(value).append("'>\n");
            sb.append(help);
            sb.append("  </div>\n");
            sb.append("</div>\n");

        }

        sb.append("<div class='control-group'>\n");
        sb.append("  <div class='controls'>\n");
        sb.append("    <button type='submit' class='btn'>Submit</button>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        sb.append("</form>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

    private CharSequence getHistory() {
        return "";
    }

    private StringBuilder getBreadcrumbHtml(Resource resource) {

        StringBuilder breadcrumb = new StringBuilder("<ul class=\"breadcrumb\">\n");

        List<Breadcrumb> breadcrumbList = getBreadcrumbList(resource);
        for (Breadcrumb bc : breadcrumbList) {
            breadcrumb.append("<li><a href=\"").append(bc.getHref()).append("\">");
            breadcrumb.append(bc.getValue()).append("</a> <span class=\"divider\">/</span></li>\n");
        }
        breadcrumb.append("</ul>\n");
        return breadcrumb;
    }

    private int handleDataElementsForList(StringBuilder sb, int i, Object object) {
        String accordionGroup = accordionGroupTemplate;
        i++;
        if (object instanceof Presentable) {
            Presentable presentable = (Presentable) object;
            accordionGroup = accordionGroup.replace("${headerText}", presentable.getHeader().getText());
            accordionGroup = accordionGroup.replace("${headerImage}", presentable.getHeader().getImage());
            accordionGroup = accordionGroup.replace("${headerCategoryIcon}", headerCategory(presentable));
            accordionGroup = accordionGroup.replace("${headerLink}", headerlink(presentable));
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

    private CharSequence getFilter() {
        return "";
    }

    private String headerlink(Presentable presentable) {
        if (presentable.getHeader().getLink() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='").append(presentable.getHeader().getLink())
                .append("'>&nbsp;<i class='icon-chevron-right'></i>&nbsp;</a>\n");
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

    private String presentations() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>\n");
        sb.append("<li><a href='?media=json'>Json</a></li>\n");
        sb.append("<li><a href='?media=xml'>XML</a></li>\n");
        sb.append("<li><a href='?media=csv'>CSV</a></li>\n");
        sb.append("</ul>\n");
        return sb.toString();
    }

    private String linkedPages(Resource resource) {

        StringBuilder sb = new StringBuilder();
        if (resource instanceof SkysailServerResource2) {
            @SuppressWarnings("unchecked")
            List<LinkedPage> pages = ((SkysailServerResource2) resource).getLinkedPages();
            for (LinkedPage page : pages) {
                if (page.applicable()) {
                    sb.append("<a class='btn btn-mini btn-link' href='").append(page.getHref()).append("' >");
                    sb.append(page.getLinkText());
                    sb.append("</a>&nbsp;");
                }
            }
        }
        return sb.toString();
    }

    private String commands(Resource resource) {

        StringBuilder sb = new StringBuilder();
        if (resource instanceof SkysailServerResource2) {
            @SuppressWarnings("unchecked")
            Map<String, Command> commandList = ((SkysailServerResource2) resource).getCommands();
            for (Entry<String, Command> command : commandList.entrySet()) {
                if (command.getValue().applicable()) {
                    sb.append("<button class=\"btn btn-mini btn-info\" type=\"submit\" name='action' value='")
                            .append(command.getKey()).append("'>").append(command.getValue().getName())
                            .append("</button>&nbsp;");
                }
            }
            if (sb.length() > 0) {
                return "<form action='?method=PUT' method='POST'>" + sb.toString() + "</form>";
            }
        }
        return "";
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

    public static String convertStreamToString(java.io.InputStream is) {
        @SuppressWarnings("resource")
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    private String calcResult(SkysailResponse<List<?>> skysailResponse) {
        if (skysailResponse instanceof ConstraintViolationsResponse) {
            return skysailResponse.getSuccess() ? "<span class=\"label label-success\">Success</span>"
                    : "<span class=\"label label-warning\">business rule violation</span>";
        }
        return skysailResponse.getSuccess() ? "<span class=\"label label-success\">Success</span>"
                : "<span class=\"label label-important\">failure</span>";
    }

}
