package de.twenty11.skysail.server.osgi.bundles;

import org.osgi.framework.Bundle;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.FormData;
import de.twenty11.skysail.common.messages.TextFieldData;
import de.twenty11.skysail.server.communication.CommunicationUtils;

public class BundleResource extends WadlServerResource {

    /** slf4j based logger implementation */
    private static final Logger logger = LoggerFactory.getLogger(BundleResource.class);

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
        String bundleName = (String) getRequest().getAttributes().get(Bundles.RESTLET_BUNDLE_NAME_ID);
        Bundle bundle = Bundles.getInstance().getBundle(bundleName);
        FormData form = new FormData();
        TextFieldData tfd = new TextFieldData("symbolicName", bundle.getSymbolicName());
        form.addField(tfd);
        tfd = new TextFieldData("location", bundle.getLocation());
        form.addField(tfd);
        
        return CommunicationUtils.createFormRepresentation(form,variant,getQuery(), getRequest(), "Bundle Info");
    }

}
