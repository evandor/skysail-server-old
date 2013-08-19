/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.common.selfdescription.ResourceDetails;
import de.twenty11.skysail.server.restlet.OSGiServiceDiscoverer;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.restlet.Timer;
import de.twenty11.skysail.server.security.SkysailRoleAuthorizer;

/**
 * trying to improve ListServerResource
 * 
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform} class and its main {@link Restlet} subclass
 * where a single instance can handle several calls concurrently, one instance of {@link ServerResource} is created for
 * each call handled and accessed by only one thread at a time.
 * 
 * @author carsten
 * 
 */
public abstract class ListServerResource2<T> extends SkysailServerResource2<T> {

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

    /** slf4j based logger implementation. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Validator validator;

    private String filterExpression;

    /**
     * return new JobDescriptor(form.getFirstValue("name"));
     */
    @Deprecated
    public abstract T getData(Form form);

    @Deprecated
    public abstract SkysailResponse<?> addEntity(T entity);

    public ListServerResource2() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    public ListServerResource2(ServerResource sr) {
        super();
        init(sr.getContext(), sr.getRequest(), sr.getResponse());
    }

    @Get("html|json|csv")
    public SkysailResponse<List<T>> getEntities() {
        return getEntities("default implementation... you might want to override ListServerResource2#getEntities in "
                + this.getClass().getName());
    }

    @Post("x-www-form-urlencoded:html|json|xml")
    public SkysailResponse<?> addFromForm(Form form) {
        sanitizeUserInput(form);
        T entity = getData(form);
        Set<ConstraintViolation<T>> violations = validate(entity);
        if (violations.size() > 0) {
            return new ConstraintViolationsResponse(entity, getOriginalRef(), violations);
        }
        return addEntity(entity);
    }

    private void sanitizeUserInput(Form form) {
        SkysailApplication app = (SkysailApplication) getApplication();
        HtmlPolicyBuilder noHtmlPolicyBuilder = app.getNoHtmlPolicyBuilder();
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

            parameter.setValue(sanitizedHtml.trim());
        }
    }

    protected abstract List<T> getData();

    protected boolean match(T t, String pattern) {
        return true;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(); // TODO: call important?
        filterExpression = getQuery() != null ? getQuery().getFirstValue("filter") : "";
    }

    protected SkysailResponse<List<T>> getEntities(String defaultMsg) {
        try {
            List<T> data = getData();
            SuccessResponse<List<T>> successResponse = new SuccessResponse<List<T>>(data);
            successResponse.setMessage(defaultMsg);
            if (this.getMessage() != null && !"".equals(this.getMessage().trim())) {
                successResponse.setMessage(getMessage());
            }
            if (getContext() != null) {
                Long executionStarted = (Long) getContext().getAttributes().get(Timer.CONTEXT_EXECUTION_STARTED);
                if (executionStarted != null) {
                    successResponse.setExecutionTime(System.nanoTime() - executionStarted);
                }
            }

            return successResponse;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<List<T>>(e);
        }
    }

    protected List<ResourceDetails> allMethods() {
        List<ResourceDetails> result = new ArrayList<ResourceDetails>();
        SkysailApplication restletOsgiApp = (SkysailApplication) getApplication();
        Map<String, RouteBuilder> skysailRoutes = restletOsgiApp.getSkysailRoutes();
        for (Entry<String, RouteBuilder> entry : skysailRoutes.entrySet()) {
            handleSkysailRoute(result, entry);
        }
        return result;
    }

    protected Validator getValidator() {
        return validator;
    }

    protected Map<String, String> getParamsFromRequest() {
        Map<String, String> params = new HashMap<String, String>();
        if (getQuery() != null) {
            params = getQuery().getValuesMap();
        }
        return params;
    }

    protected Set<ConstraintViolation<T>> validate(T entity) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(entity);
        if (violations.size() > 0) {
            logger.warn("contraint violations found on {}: {}", entity, violations);
        }
        return violations;
    }

    protected String augmentWithFilterMsg(String msg) {
        return filterExpression == null ? msg : msg + " filtered by '" + filterExpression + "'";
    }

    protected boolean filterMatches(T t) {
        if (filterExpression != null && filterExpression.trim().length() != 0) {
            return match(t, filterExpression);
        } else {
            return true;
        }
    }

    private void handleSkysailRoute(List<ResourceDetails> result, Entry<String, RouteBuilder> entry) {
        RouteBuilder builder = entry.getValue();
        if (builder.isVisible()) {
            List<SkysailRoleAuthorizer> rolesAuthorizers = builder.getRolesAuthorizers();
            Subject currentUser = SecurityUtils.getSubject();
            for (SkysailRoleAuthorizer authorizer : rolesAuthorizers) {
                if (!currentUser.hasRole(authorizer.getIdentifier())) {
                    return;
                }
            }
            String from = getHostRef() + "/" + getApplication().getName() + entry.getKey();
            String text = builder.getText() != null ? builder.getText() : from;
            String targetClass = builder.getTargetClass() == null ? "null" : builder.getTargetClass().toString();
            ResourceDetails resourceDetails = new ResourceDetails(from, text, targetClass, "desc");
            result.add(resourceDetails);
        }
    }

}
