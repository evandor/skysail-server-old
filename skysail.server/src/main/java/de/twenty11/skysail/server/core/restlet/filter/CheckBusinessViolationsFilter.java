package de.twenty11.skysail.server.core.restlet.filter;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.Request;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.server.core.restlet.ResourceFilter;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

public class CheckBusinessViolationsFilter<T> extends ResourceFilter<T> {

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
    protected int beforeHandle(Resource resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#beforeHandle", this.getClass().getSimpleName());
        return CONTINUE;
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T entity = response.getSkysailResponse().getData();
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            response.setSkysailResponse(new ConstraintViolationsResponse(entity, request.getOriginalRef(), violations));
            return STOP;
        }
        super.doHandle(resource, request, response);
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
        logger.debug("entering {}#afterHandle", this.getClass().getSimpleName());
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        if (violations.size() > 0) {
            logger.warn("constraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

}
