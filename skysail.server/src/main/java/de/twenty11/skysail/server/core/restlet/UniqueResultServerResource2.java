package de.twenty11.skysail.server.core.restlet;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.FoundIllegalInputResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;
import de.twenty11.skysail.server.restlet.SkysailApplication;

public abstract class UniqueResultServerResource2<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Validator validator;

    public UniqueResultServerResource2() {
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

    protected abstract T getData();

    public abstract T getData(Form form);

    public abstract SkysailResponse<?> addEntity(T entity);

    @Get("html|json")
    public SkysailResponse<T> getEntity() {
        return getEntity("default implementation... you might want to override UniqueResultServerResource2#getEntity in "
                + this.getClass().getName());
    }

    @Post("x-www-form-urlencoded:html|json|xml")
    public SkysailResponse<?> addFromForm(Form form) {
        if (containsInvalidInput(form)) {
            T entity = getData(form);
        	return new FoundIllegalInputResponse<T>(entity, getOriginalRef());
        }
        T entity = getData(form);
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            return new ConstraintViolationsResponse(entity, getOriginalRef(), violations);
        }
        return addEntity(entity);
    }

    protected SkysailResponse<T> getEntity(String defaultMsg) {
        try {
            T data = getData();
            SuccessResponse<T> successResponse = new SuccessResponse<T>(data);
            successResponse.setMessage(defaultMsg);
            if (this.getMessage() != null && !"".equals(this.getMessage().trim())) {
                successResponse.setMessage(getMessage());
            }
            return successResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
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

    private boolean containsInvalidInput(Form form) {
        SkysailApplication app = (SkysailApplication) getApplication();
        HtmlPolicyBuilder noHtmlPolicyBuilder = app.getNoHtmlPolicyBuilder();
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
