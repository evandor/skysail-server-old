package de.twenty11.skysail.server.core.restlet;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;

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

    @Get("html|json")
    public SkysailResponse<T> getEntity() {
        return getEntity("default implementation... you might want to override UniqueResultServerResource2#getEntity in "
                + this.getClass().getName());
    }

    protected SkysailResponse<T> getEntity(String defaultMsg) {
        try {
            T data = getData();
            SuccessResponse<T> successResponse = new SuccessResponse<T>(data);
            successResponse.setMessage(defaultMsg);
            if (this.getMessage() != null && !"".equals(this.getMessage().trim())) {
                successResponse.setMessage(getMessage());
            }
            // if (getContext() != null) {
            // Object beanAsObject = getContext().getAttributes().get(Configuration.CONTEXT_OPERATING_SYSTEM_BEAN);
            // if (beanAsObject != null && beanAsObject instanceof OperatingSystemMXBean) {
            // OperatingSystemMXBean bean = (OperatingSystemMXBean) beanAsObject;
            // successResponse.setServerLoad(bean.getSystemLoadAverage());
            // }
            // Long executionStarted = (Long) getContext().getAttributes().get(Timer.CONTEXT_EXECUTION_STARTED);
            // if (executionStarted != null) {
            // successResponse.setExecutionTime(System.nanoTime() - executionStarted);
            // }
            // }

            return successResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }

    // protected SkysailResponse<T> getEntity(T data) {
    // try {
    //
    // SkysailApplication app = (SkysailApplication) getApplication();
    // Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener()
    // .getMappings() : null;
    // return new SuccessResponse<T>(data);
    // } catch (Exception e) {
    // // logger.error(e.getMessage(), e);
    // return new FailureResponse<T>(e);
    // }
    // }

    public Validator getValidator() {
        return validator;
    }

}
