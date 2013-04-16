package de.twenty11.skysail.server.restlet;

import de.twenty11.skysail.common.responses.*;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;
import java.util.Set;

public abstract class AddServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Validator validator;

    public AddServerResource() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * return new FormResponse<T>(new T(), "../triggers/");
     */
    @Get("html")
    protected abstract FormResponse<T> createForm();

    protected abstract T getData(Form form);

    protected abstract SkysailResponse<T> addEntity(T entity);

    @Post("x-www-form-urlencoded:html")
    protected SkysailResponse<T> addFromForm(Form form) {
        T entity = getData(form);
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            return new ConstraintViolationsResponse(entity, violations);
        }
        return addEntity(entity);
    }

    private Set<ConstraintViolation<T>> validate(T entity) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(entity);
        if (violations.size() > 0) {
            logger.warn("contraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

    public Validator getValidator() {
        return validator;
    }
}
