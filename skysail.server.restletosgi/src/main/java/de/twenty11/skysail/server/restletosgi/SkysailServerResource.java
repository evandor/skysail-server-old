package de.twenty11.skysail.server.restletosgi;

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
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import de.twenty11.skysail.server.communication.CommunicationUtils;
import freemarker.template.Template;

/**
 * @author carsten
 * 
 */
public abstract class SkysailServerResource<T extends SkysailData> extends WadlServerResource {

    /** slf4j based logger implementation */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String message = "";

    private String template = "skysail.product.twindir:accounts.ftl";

    public SkysailServerResource(String message) {
        this.message = message;
    }

    public abstract T getData();

    @Get("json")
    public Representation getJson() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(message, getData());
            return new JacksonRepresentation<SkysailResponse<T>>(response);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.APPLICATION_JSON);
        }
    }

    @Get("xml")
    public Representation getXml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(message, getData());
            return new XstreamRepresentation<SkysailResponse<T>>(response);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.APPLICATION_XML);
        }
    }

    @Get("html")
    public Representation getHtml() {
        try {
            SkysailResponse<T> response = new SkysailSuccessResponse<T>(message, getData());
            response.setOrigRequest(getRequest().getOriginalRef().toUrl());
            Template ftlTemplate = CommunicationUtils.getFtlTemplate(template);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, MediaType.TEXT_HTML);
        }
    }
    
    public void setTemplate(String template) {
        this.template = template;
    }
}
