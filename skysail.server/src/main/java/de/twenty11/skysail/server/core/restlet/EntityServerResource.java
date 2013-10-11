package de.twenty11.skysail.server.core.restlet;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.data.ClientInfo;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

public abstract class EntityServerResource<T> extends SkysailServerResource<T> {

    public static final String SKYSAIL_SERVER_RESTLET_FORM = "de.twenty11.skysail.server.core.restlet.form";

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Validator validator;

    public EntityServerResource() {
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

    @Get("html|json")
    public SkysailResponse<T> getEntity() {
        return getEntity("default implementation... you might want to override UniqueResultServerResource2#getEntity in "
                + this.getClass().getName());
    }

    @Post("x-www-form-urlencoded:html|json|xml")
    public SkysailResponse<?> addFromForm(Form form) {
        ClientInfo ci = getRequest().getClientInfo();
        logger.info("calling addFromForm, media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");
        getRequest().getAttributes().put(SKYSAIL_SERVER_RESTLET_FORM, form);
        AbstractResourceFilter<EntityServerResource<T>, T> handler = RequestHandler.<T> createForEntity(Method.POST);
        return handler.handle(this, getRequest()).getSkysailResponse();
    }

    protected SkysailResponse<T> getEntity(String defaultMsg) {
        AbstractResourceFilter<EntityServerResource<T>, T> chain = RequestHandler.<T> createForEntity(Method.GET);
        ResponseWrapper<T> wrapper = chain.handle(this, getRequest());
        return wrapper.getSkysailResponse();
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
