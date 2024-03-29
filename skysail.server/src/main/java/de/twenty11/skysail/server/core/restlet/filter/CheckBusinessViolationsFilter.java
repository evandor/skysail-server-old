package de.twenty11.skysail.server.core.restlet.filter;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

public class CheckBusinessViolationsFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(CheckBusinessViolationsFilter.class);

    private final Validator validator;

    public CheckBusinessViolationsFilter() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();
        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = response.getSkysailResponse().getData();
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            response.setSkysailResponse(new ConstraintViolationsResponse(entity, request.getOriginalRef(), violations));
            return FilterResult.STOP;
        }
        super.doHandle(resource, request, response);
        return FilterResult.CONTINUE;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        // if (entity instanceof List) {
        // return listValidation((List) entity);
        // }
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (violations.size() > 0) {
            logger.warn("constraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

    // private Set<ConstraintViolation<Object>> listValidation(List<?> entity) {
    // for (Object object : entity) {
    // Set<ConstraintViolation<Object>> violations = validator.validate(object);
    // }
    //
    // }

}
