package de.twenty11.skysail.server.core.restlet;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

@Presentation(preferred = PresentationStyle.EDIT)
@Deprecated
public abstract class AddServerResource2<T> extends SkysailServerResource2<T> {

    /**
     * slf4j based logger implementation.
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Validator validator;

    public AddServerResource2() {
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
    public abstract FormResponse<T> createForm();

    /**
     * return new JobDescriptor(form.getFirstValue("name"));
     * 
     * @deprecated Post will be supported by XXXsResource
     */
    @Deprecated
    public abstract T getData(Form form);

    /**
     * @deprecated Post will be supported by XXXsResource
     */
    @Deprecated
    public abstract SkysailResponse<?> addEntity(T entity);

    /**
     * @deprecated Post will be supported by XXXsResource
     */
    @Post("x-www-form-urlencoded:html")
    @Deprecated
    public SkysailResponse<?> addFromForm(Form form) {
        T entity = getData(form);
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            return new ConstraintViolationsResponse(entity, null, violations);
        }
        return addEntity(entity);
    }

    // @Post
    // public Representation acceptRepresentation(Representation entity) {
    // if (entity instanceof InputRepresentation) {
    // ((InputRepresentation)entity).
    // }
    // return new StringRepresentation("testing post");
    // }

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
