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

package de.twenty11.skysail.server.restlet;

import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.Restlet;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.common.selfdescription.ResourceDetails;
import de.twenty11.skysail.server.internal.Configuration;

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

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Validator validator;

    private String filterExpression;

    public ListServerResource2() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    @Get("html|json|csv")
    public SkysailResponse<List<T>> getEntities() {
        return getEntities("default implementation... you might want to override ListServerResource2#getEntities in "
                + this.getClass().getName());
    }

    protected abstract List<T> getData();

    protected boolean match(T t, String pattern) {
        return true;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
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
                Object beanAsObject = getContext().getAttributes().get(Configuration.CONTEXT_OPERATING_SYSTEM_BEAN);
                if (beanAsObject != null && beanAsObject instanceof OperatingSystemMXBean) {
                    OperatingSystemMXBean bean = (OperatingSystemMXBean) beanAsObject;
                    successResponse.setServerLoad(bean.getSystemLoadAverage());
                }
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
            String from = getHostRef() + "/" + getApplication().getName() + entry.getKey();
            String text = builder.getText() != null ? builder.getText() : from;
            ResourceDetails resourceDetails = new ResourceDetails(from, text, builder.getTargetClass().toString(),
                    "desc");
            result.add(resourceDetails);
        }
    }

}
