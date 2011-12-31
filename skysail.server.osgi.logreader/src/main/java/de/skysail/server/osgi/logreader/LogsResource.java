package de.skysail.server.osgi.logreader;

import java.util.List;

import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.LinkData;
import de.twenty11.skysail.server.communication.CommunicationUtils;

public class LogsResource extends WadlServerResource {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Get("json|xml|html|txt")
    public Representation processRequest(Variant variant) {
        try {
            logger.debug("dispatching accepted media types: " + getRequest().getClientInfo().getAcceptedMediaTypes());
            LogReader handler = new LogReader();
            return dispatchGet(handler, variant);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, variant.getMediaType());
        }
    }

    private Representation dispatchGet(LogReader handler, Variant variant) {
        List<LinkData> loggerLinks = handler.getLoggerKeys();
        CommunicationUtils commUtils = new CommunicationUtils();
        // TODO check
        return null;
//        return commUtils.createLinkRepresentation(loggerLinks, variant, getQuery(), getRequest(),
//                "List of Logs");
    }

}
