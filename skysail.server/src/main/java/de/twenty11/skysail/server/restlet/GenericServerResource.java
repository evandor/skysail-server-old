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

import java.util.HashMap;
import java.util.Map;

import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.GridData;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.services.ConfigService;

/**
 * An class dealing with common functionality for a skysail server resource which is backed-up by a GridData object.
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
public class GenericServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Filter filter;

    private Integer currentPage = 1;

    private Integer pageSize = 0;

    private int totalResults;

    /**
     * @param data
     *            the skysail data backing up the resource.
     */
    public GenericServerResource(ColumnsBuilder builder) {
        super();
    }

    public void buildGrid() {
        logger.error("you should implement a subclass of GridDataServerResource and overwrite method filterData");
    }

    public int handlePagination() {
        return doHandlePagination("skysail.server.osgi.bundles.entriesPerPage", 15);
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
        //sort();

        // handle Page size and pagination
        int pageSize = handlePagination();
        setPageSize(pageSize);
        // how many results do we have (all pages)
        //setTotalResults(getSkysailData().getSize());

        // get results for current page
        return getSkysailData();//currentPageResults(pageSize);
    }

    public void setResponseDetails(SkysailResponse<GridData> response, MediaType mediaType) {
        if (response.getMessage() == null || response.getMessage().trim().equals("")) {
            response.setMessage(getMessage());
        }
        response.setTotalResults(getTotalResults());
        response.setPage(getCurrentPage());
        response.setPageSize(getPageSize());
        response.setRequest(getRequest().getOriginalRef() != null ? getRequest().getOriginalRef().toString() : null);
        response.setParent(getParent() + "?media=" + mediaType.toString().replace("application/", ""));
        response.setContextPath("/rest/");
        response.setFilter(getFilter() != null ? getFilter().toString() : "");
        //response.setSortingRepresentation(getSorting());
        if (getQuery() != null && getQuery().getNames().contains("debug")) {
            response.setDebug(true);
        }
    }

    protected int doHandlePagination(String configIdentifier, int defaultSize) {
        int pageSize = 20;
        String firstValue = getQuery() != null ? getQuery().getFirstValue("page", "1") : "1";
        int page = Integer.parseInt(firstValue);
        setCurrentPage(page);

        ConfigService configService = null;// ConfigServiceProvider.getConfigService();
        String pageSizeFromProperties = null;
        if (configService != null) {
            pageSizeFromProperties = configService.getString(configIdentifier);
        }
        if (pageSizeFromProperties != null && pageSizeFromProperties.trim().length() > 0) {
            pageSize = Integer.parseInt(pageSizeFromProperties);
        } else {
            pageSize = defaultSize;
        }
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

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
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

}
