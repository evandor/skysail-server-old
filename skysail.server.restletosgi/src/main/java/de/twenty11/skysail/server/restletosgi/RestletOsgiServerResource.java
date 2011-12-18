package de.twenty11.skysail.server.restletosgi;

import java.security.Principal;
import java.util.List;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.ext.wadl.DocumentationInfo;
import org.restlet.ext.wadl.RepresentationInfo;
import org.restlet.ext.wadl.WadlServerResource;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.SkysailData;
import de.twenty11.skysail.common.responses.SkysailFailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import freemarker.template.Template;

/**
 * @author carsten
 * 
 */
public class RestletOsgiServerResource extends WadlServerResource {

    /** slf4j based logger implementation. */
    private final Logger             logger = LoggerFactory.getLogger(this.getClass());

    /** lookup service for components. */
    private static IComponentsLookup componentsLookup;

    /**
     * setter for the components lookup.
     * 
     * @param value
     *            the provided (i.e. injected) componentLookup
     */
    public static void setLookup(final IComponentsLookup value) {
        componentsLookup = value;
    }

    /**
     * defines the entry point for 'get' restlet requests.
     * 
     * @param variant
     *            the variant(s) asked for by the client
     * @return a restful representation of the requested resource
     */
    @Get("json|xml|html|txt")
    public final Representation getRequest(final Variant variant) {
        logger.debug("dispatching accepted media types: " + getRequest().getClientInfo().getAcceptedMediaTypes());
        return handleGet(variant);
    }

    /**
     * Either deliver a response containing the request asked for or an
     * appropriate error message.
     * 
     * This is the level the error management should happen. The error response
     * should be delivered with the the variant as the original request. If it
     * is likely that specific exceptions (e.g. FileNotFound) cannot be handled
     * in a reasonable way (i.e. overcome), from this level on the exception
     * should be transformed into an unchecked one.
     * 
     * @param variant
     *            the variant(s) asked for by the client
     * @return a restful representation of either the resource asked for or a
     *         error status representation with the same variant
     */
    private Representation handleGet(final Variant variant) {
        try {
            // try to execute the command given
            return dispatchCommand(get("command"), variant);
        } catch (Exception e) {
            // if anything fails, create an error representation with the proper
            // variant
            return dispatchVariant(e, variant);
        }
    }

    /**
     * @param variant
     *            the variant(s) the client asked for
     * @return the representation of the resource
     */
    private Representation dispatchCommand(final String command, final Variant variant) {

        Request req = getRequest();
        String pathInfo = req.getResourceRef().getPath();
        Principal userPrincipal = getPrincipal(req);

        if (command == null) {
            List<SkysailData> osgiComponents = (List<SkysailData>) dispatch(userPrincipal, pathInfo, getQuery());
            return createRepresentation(osgiComponents, variant);
        }
        return null;
        //
        // if (command.equals("show")) {
        // GridData griddata = osgiBridge.osgiGetGridData(req, res);
        // return createRepresentation(griddata, variant);
        // }
        // else if (command.equals("menus")) {
        // List<TreeNodeData> menuList = osgiBridge.getMenu(req, res);
        // return createRepresentation(menuList, variant);
        // } else if (command.equals("fields")) {
        // GridInfo is = osgiBridge.getGridInfo(req, res);
        // return createRepresentation(is, variant);
        // } else {
        // InputStream is = osgiBridge.genericGet(req,res);
        // return createResponse(is, variant);
        // }
    }

    private Object dispatch(Principal userPrincipal, String pathInfo, Form form) {

        logger.info("Dispatching path '" + pathInfo + "'");

        String componentName = getComponentFromPath(pathInfo);
        EntityService component = componentsLookup.getComponent(componentName);
        String command = getCommandFromPath(pathInfo);

        if (!pathInfo.startsWith("/components/")) {
            throw new RuntimeException("pathInfo doesn't start with expected '/components/'");
        }

        if (pathInfo.equals("/components/")) {
            return componentsLookup.getComponents(userPrincipal);
        }
        return null;
        // // list of standard commands
        // if (command.equals("menus")) {
        // return serialize(component.getSubMenu(getQueryFromPath(pathInfo)));
        // } else if (command.equals("show")) {
        // return serialize(component.getGridData(userPrincipal, pathInfo,
        // parameterMap));
        // } else if (command.equals("fields")) {
        // return serialize(component.getGridInfo(pathInfo, parameterMap));
        // }
        // // last resort for non-standard commands
        // return component.runCommand(command, parameterMap);
    }

    private Representation dispatchVariant(Exception e, Variant variant) {
        logger.info("creating error representation for variant " + variant);
        SkysailResponse<SkysailData> res = new SkysailFailureResponse(e);
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = RestletOsgiApplication.getFtlTemplate("skysail.server.restletosgi:errormessage.ftl");
            return new TemplateRepresentation(ftlTemplate, res, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }

    private Principal getPrincipal(Request req) {
        List<Principal> principals = req.getClientInfo().getPrincipals();
        Principal userPrincipal = (principals == null || principals.size() == 0) ? null : principals.get(0);
        return userPrincipal;
    }

    /**
     * returns the registered / configured components for the skysail restlet
     * server.
     * 
     * A component must extend the abstract class Component.
     * 
     * @param mediaType
     *            the representation type
     * @return a string representation of the resource for the given mediaType
     */
    @Deprecated
    protected Representation createRepresentation(final List<SkysailData> data, final Variant variant) {
        SkysailResponse<SkysailData> response;
        logger.debug("creating representation for variant " + variant);
//        response = new SkysailSuccessResponse<SkysailData>("data loaded", data);
//        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
//            return new JacksonRepresentation<SkysailResponse<SkysailData>>(response);
//        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
//            Template ftlTemplate = RestletOsgiApplication.getFtlTemplate("de.evandor.skysail.server.restletosgi:menu.ftl");
//            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
//        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
//            return new XstreamRepresentation<SkysailResponse<SkysailData>>(response);
//        } else {
//            throw new RuntimeException("media type '" + variant + "' not supported");
//        }
        return null;
    }

    protected String get(String identifier) {
        return (String) getRequest().getAttributes().get(identifier);
    }

    /**
     * will return "components" assuming path looks like
     * "/components/<component>/<command>/".
     * 
     * @param pathInfo
     * @return components part or null if not able to determine
     */
    protected String getComponentFromPath(String pathInfo) {
        if (pathInfo == null)
            return null;
        String[] parts = pathInfo.split("/");
        if (parts != null && parts.length >= 3)
            return parts[2];
        return null;
    }

    /**
     * will return "command" assuming path looks like
     * "/components/<component>/<command>/".
     * 
     * @param pathInfo
     * @return command part or null if not able to determine
     */
    protected String getCommandFromPath(String pathInfo) {
        if (pathInfo == null)
            return null;
        String[] parts = pathInfo.split("/");
        if (parts != null && parts.length >= 4)
            return parts[3];
        return null;
    }

    /** === WADL related ===================================================== */

    @Override
    protected void describe(ApplicationInfo applicationInfo) {
        RepresentationInfo rep = new RepresentationInfo(MediaType.TEXT_PLAIN);
        rep.setIdentifier("component");
        applicationInfo.getRepresentations().add(rep);

        DocumentationInfo doc = new DocumentationInfo();
        doc.setTitle("Component");
        doc.setTextContent("Simple string containing the account ID");
        rep.getDocumentations().add(doc);
    }

}
