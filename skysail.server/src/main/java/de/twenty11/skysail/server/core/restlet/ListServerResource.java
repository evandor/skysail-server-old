package de.twenty11.skysail.server.core.restlet;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.restlet.Restlet;
import org.restlet.data.ClientInfo;
import org.restlet.data.Method;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;

/**
 * A ListServerResource implementation takes care of a List of Entities.
 * 
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform} class and its main {@link Restlet} subclass
 * where a single instance can handle several calls concurrently, one instance of {@link ServerResource} is created for
 * each call handled and accessed by only one thread at a time.
 * 
 * @author carsten
 * 
 */
public abstract class ListServerResource<T> extends SkysailServerResource<List<T>> {

    public static final String CONSTRAINT_VIOLATIONS = "constraintViolations";

    /** slf4j based logger implementation. */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String filterExpression;

    @Get("html|json|csv")
    public SkysailResponse<List<T>> getEntities() {
        ClientInfo ci = getRequest().getClientInfo();
        logger.info("calling getEntities, media types '{}'", ci != null ? ci.getAcceptedMediaTypes() : "test");
        return getEntities("default implementation... you might want to override ListServerResource2#getEntities in "
                + this.getClass().getName());
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit(); // TODO: call important?
        filterExpression = getQuery() != null ? getQuery().getFirstValue("filter") : "";
    }

    protected SkysailResponse<List<T>> getEntities(String defaultMsg) {
        AbstractResourceFilter<ListServerResource<T>, List<T>> handler = RequestHandler.<T> createForList(Method.GET);
        return handler.handle(this, getRequest()).getSkysailResponse();
    }

    @Override
    public SkysailResponse<?> addEntity(List<T> entity) {
        throw new NotImplementedException();
    }

    @Override
    public SkysailResponse<?> updateEntity(List<T> entity) {
        throw new NotImplementedException();
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

    protected boolean match(T t, String pattern) {
        return true;
    }

}
