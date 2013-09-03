package de.twenty11.skysail.server.core.restlet.filter;

import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FoundIllegalInputResponse;
import de.twenty11.skysail.server.core.restlet.ResourceFilter;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class CheckInvalidInputFilter<T> extends ResourceFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(CheckInvalidInputFilter.class);

    private static HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    @Override
    protected int beforeHandle(Resource resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#beforeHandle", this.getClass().getSimpleName());
        return CONTINUE;
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());

        // do in "before"?
        Form form = request.getResourceRef().getQueryAsForm();
        // needed only for tests? Better way there should be
        if (form == null) {
            form = (Form) request.getAttributes().get("form");
        }

        if (containsInvalidInput(form)) {
            T entity = resource.getData(form);
            response.setSkysailResponse(new FoundIllegalInputResponse<T>(entity, resource.getOriginalRef()));
            return STOP;
        }
        super.doHandle(resource, request, response);
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
        logger.debug("entering {}#afterHandle", this.getClass().getSimpleName());
    }

    private boolean containsInvalidInput(Form form) {
        // SkysailApplication app = (SkysailApplication) getApplication();
        // HtmlPolicyBuilder noHtmlPolicyBuilder = app.getNoHtmlPolicyBuilder();
        boolean foundInvalidInput = false;
        for (int i = 0; i < form.size(); i++) {
            Parameter parameter = form.get(i);
            String originalValue = parameter.getValue();
            StringBuilder sb = new StringBuilder();
            HtmlSanitizer.Policy policy = noHtmlPolicyBuilder.build(HtmlStreamRenderer.create(sb,
                    new Handler<String>() {
                        @Override
                        public void handle(String x) {
                            System.out.println(x);
                        }
                    }));
            HtmlSanitizer.sanitize(originalValue, policy);
            String sanitizedHtml = sb.toString();
            if (!sanitizedHtml.equals(originalValue)) {
                foundInvalidInput = true;
            }
            parameter.setValue(sanitizedHtml.trim());
        }
        return foundInvalidInput;
    }

}
