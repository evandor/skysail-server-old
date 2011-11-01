package de.twenty11.skysail.server.osgi.bundles;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import de.twenty11.skysail.server.communication.CommunicationUtils;

public class BundlesResource extends WadlServerResource {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Get("json|xml|html|txt")
    public Representation processRequest(Variant variant) {
        try {
            logger.debug("dispatching accepted media types: " + getRequest().getClientInfo().getAcceptedMediaTypes());
            return dispatchGet(variant);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, variant);
        }
    }

    private Representation dispatchGet(Variant variant) {
        GridData links = Bundles.getInstance().getBundles();

        SkysailResponse<GridData> response;
        response = new SkysailSuccessResponse<GridData>("List of Bundles", links);
        handleParameters(getQuery(), getRequest(), response);

        return CommunicationUtils.createGridDataRepresentation(response, variant, "skysail.server.osgi.bundles:bundles.ftl");
    }
    
    private static void handleParameters(Form query, Request request, SkysailResponse<?> response) {
        response.setOrigRequest(request.getOriginalRef().toUrl());
        if (query != null && query.getNames().contains("debug")) {
            response.setDebug(true);
        }
    }


}
