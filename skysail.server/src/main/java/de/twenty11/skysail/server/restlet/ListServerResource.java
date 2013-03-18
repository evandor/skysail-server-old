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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.restlet.Restlet;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.DetailsLinkProvider;
import de.twenty11.skysail.common.forms.ConstraintViolations;
import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.internal.Configuration;

/**
 * An class dealing with common functionality for a skysail server resource..
 * 
 * The class is not abstract in order to let jackson deserialize json requests more easily.
 * 
 * <br>
 * Concurrency note from parent: contrary to the {@link org.restlet.Uniform} class and its main {@link Restlet} subclass
 * where a single instance can handle several calls concurrently, one instance of {@link ServerResource} is created for
 * each call handled and accessed by only one thread at a time.
 * 
 * @author carsten
 * 
 */
public class ListServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer currentPage = 1;

    private Integer pageSize = 0;

    private int totalResults;

    private Validator validator;

    private String filterExpression;

    public ListServerResource() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
        // getVariants().add(new Variant(MediaType.TEXT_HTML));
        // getVariants().add(new Variant(MediaType.APPLICATION_JSON));

    }

    protected boolean match(T t, String pattern) {
        return true;
    }

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        filterExpression = getQuery().getFirstValue("filter");
    }

    public void buildGrid() {
        logger.error("you should implement a subclass of GridDataServerResource and overwrite method filterData");
    }

    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
    }

    protected SkysailResponse<List<T>> getEntities(List<T> data, String defaultMsg) {
        try {
            SkysailApplication app = (SkysailApplication) getApplication();
            Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener()
                    .getMappings() : null;
            Reference ref = getReference();

            for (T payload : data) {
                if (payload instanceof DetailsLinkProvider) {
                    Map<String, String> links = new HashMap<String, String>();
                    DetailsLinkProvider dlp = (DetailsLinkProvider) payload;
                    for (Entry<String, String> linkEntry : dlp.getLinkMap().entrySet()) {
                        links.put(linkEntry.getKey(), ref.toString() + linkEntry.getValue());
                    }
                    dlp.setLinks(links);
                }

            }

            SuccessResponse<List<T>> successResponse = new SuccessResponse<List<T>>(data, getRequest(), mappings);
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

    // TODO move to uniqueresultServerResource
    protected SkysailResponse<T> getEntity(T data) {
        try {
            SkysailApplication app = (SkysailApplication) getApplication();
            Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener()
                    .getMappings() : null;
            return new SuccessResponse<T>(data, getRequest(), mappings);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }

    protected SkysailResponse<String> deleteEntity(EntityManager em, T entity) {
        try {
            em.remove(entity);
            SuccessResponse<String> response = new SuccessResponse<String>(null);
            response.setMessage("deleted entity '" + entity + "'");
            return response;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<String>(e);
        }
    }

    protected SkysailResponse<T> addEntity(EntityManager em, T entity,
            Set<ConstraintViolation<T>> violations) {
        if (violations.size() > 0) {
            // if (constraintViolations.getMsg() != null) {
            logger.warn("contraint violations found on {}: {}", entity, violations);
            // return new FailureResponse<ConstraintViolations<T>>(constraintViolations);
            return new ConstraintViolationsResponse<T>(entity, violations);
        }
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            em.close();
            return new SuccessResponse<T>();
        } catch (Exception e) {
            e.printStackTrace();
            return new FailureResponse<T>(e.getMessage());
        }

    }

    protected Validator getValidator() {
        return validator;
    }

    /**
     * Implementors of this class have to provide skysailData which will be used to create a restlet representation.
     * Which type of representation (json, xml, ...) will be returned depends on the request details.
     * 
     * @return Type extending SkysailData
     * 
     */
    public final T getFilteredData() {

        // get the data, applying the current filter
        buildGrid();

        // sort the results
        // sort();

        // handle Page size and pagination
        int pageSize = handlePagination();
        setPageSize(pageSize);
        // how many results do we have (all pages)
        // setTotalResults(getSkysailData().getSize());

        // get results for current page
        return getSkysailData();// currentPageResults(pageSize);
    }

    protected int doHandlePagination(String configIdentifier, int defaultSize) {
        int pageSize = 20;
        String firstValue = getQuery() != null ? getQuery().getFirstValue("page", "1") : "1";
        int page = Integer.parseInt(firstValue);
        setCurrentPage(page);

        pageSize = defaultSize;
        String pageSizeParam = getQuery() != null ? getQuery().getFirstValue("pageSize", null) : null;
        if (pageSizeParam != null) {
            pageSize = Integer.parseInt(pageSizeParam);
        }

        return pageSize;
    }

    protected Map<String, String> getParamsFromRequest() {
        Map<String, String> params = new HashMap<String, String>();
        if (getQuery() != null) {
            params = getQuery().getValuesMap();
        }
        return params;
    }

    protected Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    protected Integer getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    protected void setTotalResults(int length) {
        this.totalResults = length;
    }

    public int getTotalResults() {
        return totalResults;
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

}
