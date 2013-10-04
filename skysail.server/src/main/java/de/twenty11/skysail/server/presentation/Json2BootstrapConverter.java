package de.twenty11.skysail.server.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.osgi.framework.BundleContext;
import org.restlet.Application;
import org.restlet.Request;
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
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.commands.Command;
import de.twenty11.skysail.common.navigation.LinkedPage;
import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource2;
import de.twenty11.skysail.server.internal.ApplicationsService;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.utils.IOUtils;

public class Json2BootstrapConverter extends ConverterHelper {

    private static final Logger logger = LoggerFactory.getLogger(Json2BootstrapConverter.class);
    private final String rootTemplate;
    private final String d3SimpleGraphTemplate;

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_JSON);

    public Json2BootstrapConverter() {

        InputStream bootstrapTemplateResource = this.getClass().getResourceAsStream("bootstrap.template");
        rootTemplate = IOUtils.convertStreamToString(bootstrapTemplateResource);
        try {
            bootstrapTemplateResource.close();
        } catch (IOException e) {
            logger.error("Problem closing resource", e);
        }

        InputStream d3SimpleGraphTemplateResource = this.getClass().getResourceAsStream("d3SimpleGraph.template");
        d3SimpleGraphTemplate = IOUtils.convertStreamToString(d3SimpleGraphTemplateResource);
        try {
            d3SimpleGraphTemplateResource.close();
        } catch (IOException e) {
            logger.error("Problem closing resource", e);
        }
    }

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
            result = 1.0F;
        } else if (target.getMediaType().equals(SkysailApplication.SKYSAIL_HTMLFORM_MEDIATYPE)) {
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
        throw new UnsupportedOperationException("not implemented");
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
        breadcrumbs.add(new Breadcrumb("/", null, "<i class='icon-home'></i>"));

        Reference reference = resource.getReference();
        SkysailApplication application = (SkysailApplication) resource.getApplication();
        RouteList routes = application.getRoutes();

        List<String> segments = getCleanedSegments(reference);

        Reference rootRef = resource.getRootRef();
        if (!(resource.getApplication() instanceof DefaultSkysailApplication)) {
            breadcrumbs.add(new Breadcrumb(segments.size() <= 1 ? null : rootRef.toString(), null, resource
                    .getApplication().getName()));
        }

        String path = "";
        Route match = null;
        for (int i = 1; i < segments.size(); i++) {
            path = path + "/" + segments.get(i);

            Request request = resource.getRequest();
            request.setResourceRef(new Reference(path));
            Route best = routes.getBest(request, resource.getResponse(), 0.5f);
            if (best != match) {
                match = best;
                if (i < segments.size() - 1) {
                    breadcrumbs.add(new Breadcrumb("/" + resource.getApplication().getName() + path, null, segments
                            .get(i)));
                } else {
                    breadcrumbs.add(new Breadcrumb(null, null, segments.get(i)));
                }
            }
        }
        return breadcrumbs;
    }

    private List<String> getCleanedSegments(Reference reference) {
        List<String> segments = reference.getSegments();
        List<String> results = new ArrayList<String>();
        for (String segment : segments) {
            if (!StringUtils.isBlank(segment)) {
                results.add(segment);
            }
        }
        return results;
    }

    private String jsonToHtml(SkysailResponse<List<?>> skysailResponse, Resource resource) {

        PresentationStyle styleOldp = ConverterUtils.evalPresentationStyle(resource);
        PresentationStyle style = skysailResponse.getPresentationStyleHint();

        String page = rootTemplate;
        long executionTimeInNanos = skysailResponse.getExecutionTime();
        float performance = new Long(1000000000) / executionTimeInNanos;
        page = page.replace("${performance}", String.format("%s", performance));
        page = page.replace("${result}", calcResult(skysailResponse));
        page = page.replace("${message}", skysailResponse.getMessage() == null ? "no message available"
                : skysailResponse.getMessage());
        page = page.replace("${linkedPages}", linkedPages(resource));
        page = page.replace("${commands}", commands(resource));
        page = page.replace("${presentations}", presentations());
        page = page.replace("${filterExpression}", getFilter());
        page = page.replace("${history}", getHistory());
        page = page.replace("${mainNav}",
                getMainNav(((SkysailApplication) resource.getApplication()).getBundleContext()));

        String username = "unknown";
        // if (resource.getRequest().getChallengeResponse() != null) {
        // username = resource.getRequest().getChallengeResponse().getIdentifier();
        // }
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipal() != null) {
            username = subject.getPrincipal().toString();
        }
        page = page.replace("${username}", "<li><a href='#'><i class=\"icon-user icon-white\"></i>&nbsp;" + username
                + "</a></li>\n");

        if (subject.getPrincipal() != null) {
            page = page.replace("${loginLogout}", "<a href='/logout'>[Logout]</a>");
        } else {
            page = page.replace("${loginLogout}", "<a href='/login'>[Login]</a>");
        }

        page = page.replace("${productName}",
                ((SkysailApplication) resource.getApplication()).getConfigForKey("productName"));

        Object skysailResponseAsObject = skysailResponse.getData();
        if (skysailResponseAsObject != null) {
            if (style.equals(PresentationStyle.LIST)) {
                StrategyContext context = new StrategyContext(new ListForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.LIST2)) {
                StrategyContext context = new StrategyContext(new ListForContentStrategy2(
                        ((SkysailApplication) resource.getApplication()).getBundleContext(), resource));
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.TABLE)) {
                StrategyContext context = new StrategyContext(new TableForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.EDIT)) {
                StrategyContext context = new StrategyContext(new FormForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.D3_SIMPLE_GRAPH)) {
                page = createD3SimpleGraphForContent(skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.IFRAME)) {
                StrategyContext context = new StrategyContext(new IFrameForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            } else if (style.equals(PresentationStyle.ACE_EDITOR)) {
                StrategyContext context = new StrategyContext(new AceEditorForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
            }
        } else {
            if (skysailResponse instanceof ConstraintViolationsResponse) {
                StrategyContext context = new StrategyContext(new FormForContentStrategy());
                page = context.createHtml(page, skysailResponseAsObject, skysailResponse);
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

    private String getMainNav(BundleContext bundleContext) {
        StringBuilder sb = new StringBuilder();
        List<Application> applications = ApplicationsService.getApplications(bundleContext);
        for (Application application : applications) {
            if (application.getName().equalsIgnoreCase("static") || application.getName().equalsIgnoreCase("default")) {
                continue;
            }
            String name = application.getName().substring(0, 1).toUpperCase() + application.getName().substring(1);
            sb.append("<li><a href='/").append(application.getName()).append("'>").append(name).append("</a></li>\n");
        }
        return sb.toString();
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
        template = template.replace("${links}", links.toString());
        return template;
    }

    private CharSequence getHistory() {
        return "";
    }

    private StringBuilder getBreadcrumbHtml(Resource resource) {

        StringBuilder breadcrumb = new StringBuilder("<ul class=\"breadcrumb\">\n");

        List<Breadcrumb> breadcrumbList = getBreadcrumbList(resource);
        int cnt = 0;
        for (Breadcrumb bc : breadcrumbList) {
            if (bc.getHref() != null) {
                breadcrumb.append("<li><a href=\"").append(bc.getHref()).append("\">");
                breadcrumb.append(bc.getValue()).append("</a>");
            } else {
                breadcrumb.append("<li><b>");
                breadcrumb.append(bc.getValue()).append("</b>");
            }
            if (++cnt < breadcrumbList.size()) {
                breadcrumb.append(" <span class=\"divider\">/</span></li>\n");
            }
        }
        breadcrumb.append("</ul>\n");
        return breadcrumb;
    }

    private CharSequence getFilter() {
        return "";
    }

    private String presentations() {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>\n");
        sb.append("<li><a href='?media=json'>Json</a></li>\n");
        sb.append("<li><a href='?media=xml'>XML</a></li>\n");
        sb.append("<li><a href='?media=csv'>CSV</a></li>\n");
        sb.append("<li><a href='?media=pdf'>PDF</a></li>\n");
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

    private String calcResult(SkysailResponse<List<?>> skysailResponse) {
        if (skysailResponse instanceof ConstraintViolationsResponse) {
            return skysailResponse.getSuccess() ? "<span class=\"label label-success\">Success</span>"
                    : "<span class=\"label label-warning\">business rule violation</span>";
        }
        return skysailResponse.getSuccess() ? "<span class=\"label label-success\">Success</span>"
                : "<span class=\"label label-important\">failure</span>";
    }

}
