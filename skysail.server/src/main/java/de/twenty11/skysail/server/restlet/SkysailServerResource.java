package de.twenty11.skysail.server.restlet;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.SkysailData;
import de.twenty11.skysail.common.responses.SkysailFailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import de.twenty11.skysail.server.communication.CommunicationUtils;

/**
 * By implementing this class you can provide RESTful access to a specific resource, i.e. a RESTful representation of
 * the resource.
 * 
 * The type of the representation depends on the request. Currently JSON, XML and HTML are supported.
 * 
 * If an exception occurs, skysail will create an error-representation with information about the exception with the
 * same type (Json, xml,...).
 * 
 * @param <T>
 *            has to extend the marker interface SkysailData.
 */
public abstract class SkysailServerResource<T extends SkysailData> extends WadlServerResource {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** short message to be passed to the client. */
    private String message = "";

    /** template to be shown. */
    private String template = "skysail.product.twindir:accounts.ftl";

    /** the payload. */
    private T skysailData;

    /**
     * Constructor expecting an object of type T (which will become the payload of the resource representation.
     * 
     * @param data
     *            for example new GridData()
     */
    public SkysailServerResource(T data) {
        this.skysailData = data;
    }

    /**
     * to be implemented by extending classes.
     * 
     * @param response
     * @param applicationXml
     */
    public abstract void setResponseDetails(SkysailResponse<T> response, MediaType applicationXml);

    /**
     * to be implemented by extending classes.
     * 
     * @return SkysailData
     */
    public abstract T getFilteredData();

    /**
     * Reasoning: not overwriting those two (overloaded) methods gives me a jackson deserialization issue. I need to
     * define which method I want to be ignored by jackson.
     * 
     * @see org.restlet.resource.ServerResource#setLocationRef(org.restlet.data.Reference)
     */
    @JsonIgnore
    @Override
    public void setLocationRef(Reference locationRef) {
        super.setLocationRef(locationRef);
    }

    @Override
    public void setLocationRef(String locationUri) {
        super.setLocationRef(locationUri);
    }

    @Get("json")
    public Representation getJson() {
        SkysailResponse<T> response = createResponse();
        setResponseDetails(response, MediaType.APPLICATION_JSON);
        return new JacksonRepresentation<SkysailResponse<T>>(response);
    }

    @Get("xml")
    public Representation getXml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(getFilteredData());
            setResponseDetails(response, MediaType.APPLICATION_XML);
            return new XstreamRepresentation<SkysailResponse<T>>(response);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.APPLICATION_XML);
        }
    }

    @Get("html")
    public Representation getHtml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(getFilteredData());
            setResponseDetails(response, MediaType.TEXT_HTML);
            // Template ftlTemplate = CommunicationUtils.getFtlTemplate(template);
            return null;// new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.TEXT_HTML);
        }
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    protected String getParent() {
        if (getRequest() == null)
            return null;
        if (getRequest().getOriginalRef() == null)
            return null;
        if (getRequest().getOriginalRef().getParentRef() == null)
            return null;
        return getRequest().getOriginalRef().getParentRef().toString();
    }

    public T getSkysailData() {
        return skysailData;
    }

    public void setSkysailData(T skysailData) {
        this.skysailData = skysailData;
    }

    protected SkysailResponse<T> createResponse() {
        SkysailResponse<T> response;
        try {
            response = new SkysailSuccessResponse<T>(getFilteredData());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response = new SkysailFailureResponse<T>(e);
        }
        return response;
    }

}
