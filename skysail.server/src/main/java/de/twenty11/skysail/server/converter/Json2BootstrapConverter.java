package de.twenty11.skysail.server.converter;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;
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
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.internal.Configuration.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.SkysailApplication;

public class Json2BootstrapConverter extends ConverterHelper {

    private ObjectMapper m = new ObjectMapper();

    private InputStream bootstrapTemplateResource = this.getClass().getResourceAsStream("bootstrap.template");
    private final String rootTemplate = convertStreamToString(bootstrapTemplateResource);

    private InputStream accordionGroupTemplateResource = this.getClass().getResourceAsStream("accordionGroup.template");
    private final String accordionGroupTemplate = convertStreamToString(accordionGroupTemplateResource);

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_JSON);

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.Response)) {
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

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        Representation representation;
        try {
            representation = new StringRepresentation(jsonToHtml((Response) source, resource));
        } catch (Exception e) {
            representation = new StringRepresentation(jsonToHtml(new FailureResponse(e), resource));
        }
        representation.setMediaType(MediaType.TEXT_HTML);
        return representation;
    }

    private String jsonToHtml(Response<List<?>> skysailResponse, Resource resource) {
        String page = rootTemplate;
        // template = template.replace("${originalJson}", json);
        long executionTimeInNanos = skysailResponse.getExecutionTime();
        float performance = new Long(1000000000) / executionTimeInNanos;
        page = page.replace("${performance}", String.format("%s", performance));
        page = page.replace("${serverLoad}", String.format("%s", skysailResponse.getServerLoad()));
        page = page.replace("${result}",
                skysailResponse.getSuccess() ? "<span class=\"label label-success\">Success</span>"
                        : "<span class=\"label label-important\">failure</span>");
        page = page.replace("${message}", skysailResponse.getMessage() == null ? "no message available"
                : skysailResponse.getMessage());
        page = page.replace("${presentations}", presentations());
        page = page.replace("${filterExpression}", getFilter());
        page = page.replace("${history}", getHistory());

        Object skysailResponseAsObject = skysailResponse.getData();
        if (skysailResponseAsObject != null) {
            StringBuilder sb = new StringBuilder();
            if (skysailResponseAsObject instanceof List) {
                List<?> data = (List<?>) skysailResponseAsObject;
                int i = 0;
                if (data != null) {
                    for (Object object : data) {
                        i = handleDataElements(sb, i, object);
                    }
                }
            } else {
                handleDataElements(sb, 1, skysailResponseAsObject);
            }
            page = page.replace("${accordionGroups}", sb.toString());
        } else {
            page = page.replace("${accordionGroups}", "");
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

    private int handleDataElements(StringBuilder sb, int i, Object object) {
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

    private CharSequence getFilter() {
        return "";
    }

    private String headerlink(Presentable presentable) {
        if (presentable.getHeader().getLink() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='").append(presentable.getHeader().getLink()).append("'>&nbsp;<i class='icon-chevron-right'></i>&nbsp;</a>\n");
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
                        + hex + ", " + hex + ");\">")
                .append(text == null ? "&nbsp;" : text)
                .append("</button>\n");
        return sb.toString();
    }

    private String presentations() {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='?media=json'>Json</a>\n");
        return sb.toString();
    }

    private String getInner(Presentable presentable) {
        StringBuilder sb = new StringBuilder("<table class=\"table table-hover\" style='width:90%'>\n");
        for (Entry<String, Object> row : presentable.getContent().entrySet()) {
            sb.append("<tr>\n");
            sb.append("<th style='width:200px;'>").append(row.getKey()).append("</th>");
            sb.append("<td style='width:600px;'>").append(row.getValue()).append("</td>\n");
            sb.append("</td>\n");
        }
        return sb.append("</table>\n").toString();
    }

    public static String convertStreamToString(java.io.InputStream is) {
        @SuppressWarnings("resource")
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
