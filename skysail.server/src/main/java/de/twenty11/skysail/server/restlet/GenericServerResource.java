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

import org.restlet.Restlet;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.responses.SuccessResponse;

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
public class GenericServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Integer currentPage = 1;

    private Integer pageSize = 0;

    private int totalResults;

    public void buildGrid() {
        logger.error("you should implement a subclass of GridDataServerResource and overwrite method filterData");
    }

    protected Response<T> getEntities(T data) {
        try {
            return new SuccessResponse<T>(data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }
    
    protected Response<T> getEntity(T singleResult) {
        try {
            return new SuccessResponse<T>(singleResult);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
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
