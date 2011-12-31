package de.twenty11.skysail.server.restletosgi;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.ext.wadl.MethodInfo;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.LinkData;
import de.twenty11.skysail.server.communication.CommunicationUtils;

/**
 * @author carsten
 * 
 */
@Deprecated
public class SkysailRootServerResource extends WadlServerResource {

    /** slf4j based logger implementation */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Get("json|xml|html|txt")
    public Representation processRequest(Variant variant) {
        try {
            logger.debug("dispatching accepted media types: " + getRequest().getClientInfo().getAcceptedMediaTypes());
            return dispatchGet(variant);
        } catch (Exception e) {
            return CommunicationUtils.createErrorResponse(e, logger, variant.getMediaType());
        }
    }

    protected Representation dispatchGet(Variant variant) {
        List<LinkData> links = new ArrayList<LinkData>();
        addLink(links,"components/", "Installed Components");
        addLink(links,"logs/", "Current Logs");
        addLink(links,"bundles/", "Installed Bundles");
        //return CommunicationUtils.createLinkRepresentation(links, variant, getQuery(), getRequest(), "Root");
        CommunicationUtils commUtils = new CommunicationUtils();
        // TODO check
        return null; //commUtils.createLinkRepresentation(links, variant, getQuery(), getRequest(), "root");
    }


    /**
     * Initializing the WadlServerResource with description etc.
     * 
     * @see org.restlet.ext.wadl.WadlServerResource#doInit()
     */
    @Override
    protected final void doInit() {
        setAutoDescribing(false);
        setDescription("Entry point for the skysail server");
        setName("Skysail Root");
    }

    @Override
    protected final RepresentationInfo describe(final MethodInfo methodInfo, final Class<?> representationClass,
            final Variant variant) {

        RepresentationInfo result = new RepresentationInfo(MediaType.TEXT_PLAIN);
        result.setIdentifier("root");

        DocumentationInfo doc = new DocumentationInfo();
        doc.setTitle("Skysail Root Application");
        doc.setTextContent("Simple 'where to go from here' html representation at the root level of the OSGi-based skysail server.");
        result.getDocumentations().add(doc);
        return result;
    }

    /**
     * @return a string representation
     */
    @Get
    public final Representation represent() {
        StringBuffer sb = new StringBuffer("<h3>Welcome to Skysail</h3>")
                .append("If you see this, you're running the OSGi-based version of the skysail server.<br>")
                .append("To get more information about skysail, please use the following links:")
                .append("<h4>Your running installation</h4>")
                .append("<a href='?method=OPTIONS' target='_blank'>WADL Documentation</a><br>")
                .append("<a href='components/?media=html'>Installed components</a><br>")
                .append("<a href='logs/?media=html'>Logs</a><br>")
                .append("<a href='bundles/?media=html'>Bundles</a><br>")
                .append("<h4>Skysail on the internet</h4>")
                .append("<a href='http://www.evandor.de:8083' target='_blank'>Wiki</a>");
        return new StringRepresentation(sb.toString(), MediaType.TEXT_HTML);
    }

    protected void addLink(List<LinkData> loggerLinks, String string, String string2) {
        LinkData ld = new LinkData(string, string2);
        loggerLinks.add(ld);
    }

}
