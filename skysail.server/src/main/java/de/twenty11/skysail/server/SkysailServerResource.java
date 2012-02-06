package de.twenty11.skysail.server;

import java.net.URL;
import java.util.List;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.SkysailData;
import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.grids.ColumnDefinition;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import de.twenty11.skysail.server.communication.CommunicationUtils;
import de.twenty11.skysail.server.internal.ConfigServiceProvider;
import de.twenty11.skysail.server.servicedefinitions.ConfigService;
import freemarker.template.Template;

/**
 * By implementing this class you can provide RESTful access to a specific resource, i.e.
 * a RESTful representation of the resource.
 * 
 * The type of the representation depends on the request. Currently JSON, XML and HTML are
 * supported.
 * 
 * If an exception occurs, skysail will create an error-representation with information 
 * about the exception with the same type (Json, xml,...). 
 * 
 * @param <T> has to extend the marker interface SkysailData.
 */
public abstract class SkysailServerResource<T extends SkysailData> extends WadlServerResource {

    /** slf4j based logger implementation */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** short message to be passed to the client */
    private String message = "";

    /** template to be shown. */
    private String template = "skysail.product.twindir:accounts.ftl";

    /** the payload */
    private T skysailData;
    
    private int totalResults;
    
    private Filter filter;

    private Integer currentPage = 1;

    private Integer pageSize;

    /**
     * Constructor expecting an object of type T (which will become the payload of the resource representation.
     * @param data for example new GridData()
     */
    public SkysailServerResource(T data) {
        this.skysailData = data;
    }

    public abstract void setColumns(T data);
    
    public abstract List<?> getFilteredData();

    public abstract int handlePagination();
    
    public abstract T currentPageResults(List<?> filterResults, int pageSize);
    
    /**
     * Implementors of this class have to provide skysailData which will be used to create
     * a restlet representation. Which type of representation (json, xml, ...) will
     * be returned depends on the request details.
     * 
     * @return Type extending SkysailData
     *
     */
    private final T getData() {
        // define the columns for the result (for grids)
        setColumns(skysailData); 
        // get actual data, applying the current filter
        List<?> filterResults = getFilteredData();
        // handle Page size and pagination
        int pageSize = handlePagination();
        setPageSize(pageSize);
        // how many results do we have (all pages)
        setTotalResults(filterResults != null ? filterResults.size() : 0);
        // get results for current page
        return currentPageResults(filterResults, pageSize);
    }

    @Get("json")
    public Representation getJson() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(getData());
            setResponseDetails(response);
            return new JacksonRepresentation<SkysailResponse<T>>(response);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.APPLICATION_JSON);
        }
    }

    @Get("xml")
    public Representation getXml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(getData());
            setResponseDetails(response);
            return new XstreamRepresentation<SkysailResponse<T>>(response);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.APPLICATION_XML);
        }
    }

    @Get("html")
    public Representation getHtml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(getData());
            setResponseDetails(response);
            Template ftlTemplate = CommunicationUtils.getFtlTemplate(template);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.TEXT_HTML);
        }
    }

    private void setResponseDetails(SkysailResponse<T> response) {
        response.setMessage(getMessage());
        response.setTotalResults(getTotalResults());
        response.setPage(getCurrentPage());
        response.setPageSize(getPageSize());
        response.setOrigRequest(getRequest().getOriginalRef().toUrl());
        response.setParent(getParent());
        response.setContextPath("/rest/");
        response.setFilter(getFilter() != null ? getFilter().toString() : "");
        if (getQuery() != null && getQuery().getNames().contains("debug")) {
            response.setDebug(true);
        }
    }
    
    private Integer getPageSize() {
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

    public void setTemplate(String template) {
        this.template = template;
    }
    
    public void setMessage(String message) {
        this.message =  message;
    }
    
    public String getMessage() {
        return message;
    }
    
    protected void setTotalResults(int length) {
        this.totalResults = length;        
    }
    
    public int getTotalResults() {
        return totalResults;
    }
    
    public Filter getFilter() {
        return filter;
    }
    
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    
    private URL getParent() {
        URL origRequest = getRequest().getOriginalRef().getParentRef().toUrl();
        return origRequest;
    }
    
    public T getSkysailData() {
        return skysailData;
    }

    protected int doHandlePagination(String configIdentifier, int defaultSize) {
        int pageSize = 20;
        String firstValue = getQuery().getFirstValue("page", "1");
        int page = Integer.parseInt(firstValue);
        setCurrentPage(page);
        
        ConfigService configService = ConfigServiceProvider.getConfigService();
        String pageSizeFromProperties = configService.getString(configIdentifier);
        if (pageSizeFromProperties != null && pageSizeFromProperties.trim().length() > 0) {
            pageSize = Integer.parseInt(pageSizeFromProperties);
        } else {
            pageSize = defaultSize;
        }
        
        return pageSize;
    }

}
