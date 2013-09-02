package de.twenty11.skysail.server.core.restlet;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

public abstract class UniqueResultServerResource<T> extends SkysailServerResource<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Validator validator;

    public UniqueResultServerResource() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    /**
     * If you have a route defined as "/repository/{key}", you can get the key like this: key = (String)
     * getRequest().getAttributes().get("key");
     * 
     * To get hold on any parameters passed, consider using this pattern:
     * 
     * Form form = new Form(getRequest().getEntity()); action = form.getFirstValue("action");
     * 
     * @see de.twenty11.skysail.server.core.restlet.SkysailServerResource2#doInit()
     */
    @Override
    protected abstract void doInit() throws ResourceException;

    public abstract T getData();

    public abstract T getData(Form form);

    public abstract SkysailResponse<?> addEntity(T entity);

    @Get("html|json")
    public SkysailResponse<T> getEntity() {
        return getEntity("default implementation... you might want to override UniqueResultServerResource2#getEntity in "
                + this.getClass().getName());
    }

    protected SkysailResponse<T> getEntity(String defaultMsg) {

        RequestHandler<T> requestHandler = new RequestHandler<T>();
        SkysailRequestHandlingFilter<T> chain = requestHandler.getChain(Method.GET);

        ResponseWrapper<T> wrapper = chain.handle(this, getRequest());
        return wrapper.getSkysailResponse();

        // try {
        // T data = getData();
        // SuccessResponse<T> successResponse = new SuccessResponse<T>(data);
        // successResponse.setMessage(defaultMsg);
        // if (this.getMessage() != null && !"".equals(this.getMessage().trim())) {
        // successResponse.setMessage(getMessage());
        // }
        // return successResponse;
        // } catch (Exception e) {
        // logger.error(e.getMessage(), e);
        // return new FailureResponse<T>(e);
        // }
    }

    public Validator getValidator() {
        return validator;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(entity);
        if (violations.size() > 0) {
            logger.warn("contraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

}
