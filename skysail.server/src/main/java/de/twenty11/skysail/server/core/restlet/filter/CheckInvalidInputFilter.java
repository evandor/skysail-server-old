package de.twenty11.skysail.server.core.restlet.filter;

import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class CheckInvalidInputFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(CheckInvalidInputFilter.class);

    private static HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());

        // do in "before"?
        Form form = (Form) request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);

        if (containsInvalidInput(form)) {
            logger.info("Input was sanitized");
        }
        super.doHandle(resource, request, response);
        return FilterResult.CONTINUE;
    }

    private boolean containsInvalidInput(Form form) {
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
