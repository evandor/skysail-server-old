package de.twenty11.skysail.server.core.restlet.filter;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FoundIllegalInputResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailRequestHandlingFilter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import java.util.Set;

public class CheckBusinessViolationsFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(CheckBusinessViolationsFilter.class);
    private final Validator validator;

    public CheckBusinessViolationsFilter () {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();
        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {

        T entity = response.getSkysailResponse().getData();
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            response.setSkysailResponse(new ConstraintViolationsResponse(entity, request.getOriginalRef(), violations));
            return STOP;
        }
        super.doHandle(resource, request, response);
        return CONTINUE;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (violations.size() > 0) {
            logger.warn("constraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

}
