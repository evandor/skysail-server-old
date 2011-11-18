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
package de.twenty11.skysail.server.communication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.freemarker.TemplateRepresentation;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.ext.xstream.XstreamRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.SkysailData;
import de.twenty11.skysail.common.messages.FormData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.messages.LinkData;
import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.common.responses.SkysailFailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SkysailSuccessResponse;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Graef
 * 
 */
public class CommunicationUtils {

    private static final String SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL = "skysail.server.restletosgi:menu.ftl";

    private static final Logger  logger = LoggerFactory.getLogger(CommunicationUtils.class);

    private static Configuration configuration;

    private String template = SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL;

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(
            value = "ST_WRITE_TO_STATIC_FROM_INSTANCE_METHOD", 
            justification = "Spring setter, object needed for all instances")
    public void setConfiguration(Configuration freemarkerConfiguration) {
        CommunicationUtils.configuration = freemarkerConfiguration;
    }
    
    public CommunicationUtils() {    }
    
    public CommunicationUtils(String freemarkerTemplate) {
        this.template = freemarkerTemplate;
    }
    
    /** === handle TreeNodeData ======================================== */

    /**
     * Convenience method
     * 
     * @param data
     * @param variant
     * @param message
     * @return
     */
    public static Representation createTreeNodeRepresentation(final TreeNodeData data, final Variant variant,
            final Form query, final Request request, String message) {
        List<TreeNodeData> oneElementList = new ArrayList<TreeNodeData>();
        return createTreeNodeRepresentation(oneElementList, variant, query, request, message);
    }

    /**
     * returns the registered / configured components for the skysail restlet
     * server.
     * 
     * A component must extend the abstract class Component.
     * 
     * @param message
     * 
     * @param mediaType
     *            the representation type
     * @return a string representation of the resource for the given mediaType
     */
    public static Representation createTreeNodeRepresentation(final List<TreeNodeData> data, final Variant variant,
            final Form query, final Request request, final String message) {
        SkysailResponse<TreeNodeData> response;
        logger.debug("creating representation for variant " + variant);
        response = new SkysailSuccessResponse<TreeNodeData>(message, data);
        handleParameters(query, request, response);

        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<TreeNodeData>>(response);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate(SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new XstreamRepresentation<SkysailResponse<TreeNodeData>>(response);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }

    /** === handle LinkData =================================== */

    /**
     * Convenience method
     * 
     * @param data
     * @param variant
     * @param message
     * @return
     */
    public Representation createLinkRepresentation(final LinkData data, final Variant variant, Form query,
            Request request, String message) {
        List<LinkData> oneElementList = new ArrayList<LinkData>();
        return createLinkRepresentation(oneElementList, variant, query, request, message);
    }

    /**
     * @param data
     * @param variant
     * @param query
     * @param request
     * @param message
     * @return
     */
    public Representation createLinkRepresentation(List<LinkData> data, Variant variant, Form query,
            Request request, String message) {
        SkysailResponse<LinkData> response;
        response = new SkysailSuccessResponse<LinkData>(message, data);
        handleParameters(query, request, response);

        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<LinkData>>(response);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate(template);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new XstreamRepresentation<SkysailResponse<LinkData>>(response);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }

    /**
     * === handle GridData ===============================================
     * 
     * @param message
     */

    public static Representation createGridInfoRepresentation(GridData data, Variant variant, Form query,
            Request request, String message) {
        SkysailResponse<GridData> response;
        response = new SkysailSuccessResponse<GridData>(message, data);
        handleParameters(query, request, response);

        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<GridData>>(response);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate(SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new XstreamRepresentation<SkysailResponse<GridData>>(response);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }

    /**
     * === handle FormData ===============================================
     * 
     * @param query
     * @param request
     * @param message
     */

    public static Representation createFormRepresentation(FormData data, Variant variant, Form query, Request request,
            String message) {
        SkysailResponse<FormData> response;
        response = new SkysailSuccessResponse<FormData>(message, data);
        response.setOrigRequest(request.getOriginalRef().toUrl());
        handleParameters(query, request, response);

        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<FormData>>(response);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate("skysail.server.restletosgi:form.ftl");
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new XstreamRepresentation<SkysailResponse<FormData>>(response);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }
    
    /**
     * === handle Errors
     * =================================================================
     */

    public static Representation createErrorResponse(final Exception e, final org.slf4j.Logger logger, Variant variant) {
        logger.info("creating error representation for variant " + variant);
        SkysailResponse<SkysailData> res = new SkysailFailureResponse(e);
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate("skysail.server.restletosgi:errormessage.ftl");
            return new TemplateRepresentation(ftlTemplate, res, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }
    
    public static Representation createErrorResponse(final Exception e, final org.slf4j.Logger logger, MediaType mediaType) {
        //logger.info("creating error representation for variant " + variant);
        SkysailResponse<SkysailData> res = new SkysailFailureResponse(e);
        if (mediaType.equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else if (mediaType.equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate("skysail.server.restletosgi:errormessage.ftl");
            return new TemplateRepresentation(ftlTemplate, res, MediaType.TEXT_HTML);
        } else if (mediaType.equals(MediaType.TEXT_XML)) {
            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
        } else {
            throw new RuntimeException("media type '" + mediaType + "' not supported");
        }
    }

    public static final Template getFtlTemplate(String templatePath) {
        // ServiceReference serviceRef =
        // bundleContext.getServiceReference(Configuration.class.getName());
        // Configuration service =
        // (Configuration)bundleContext.getService(serviceRef);
        if (configuration != null) {
            try {
                return configuration.getTemplate(templatePath);
            } catch (IOException e) {
                throw new RuntimeException("Problem accessing template '" + templatePath + "'");
            }

        }
        return null;
    }

    private static void handleParameters(Form query, Request request, SkysailResponse<?> response) {
        response.setOrigRequest(request.getOriginalRef().toUrl());
        if (query != null && query.getNames().contains("debug")) {
            response.setDebug(true);
        }
    }

    public static Representation createGridDataRepresentation(SkysailResponse<GridData> response, Variant variant,
            String ftlPath) {
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON)) {
            return new JacksonRepresentation<SkysailResponse<GridData>>(response);
        } else if (variant.getMediaType().equals(MediaType.TEXT_HTML)) {
            Template ftlTemplate = getFtlTemplate(ftlPath);
            return new TemplateRepresentation(ftlTemplate, response, MediaType.TEXT_HTML);
        } else if (variant.getMediaType().equals(MediaType.TEXT_XML)) {
            return new XstreamRepresentation<SkysailResponse<GridData>>(response);
        } else {
            throw new RuntimeException("media type '" + variant + "' not supported");
        }
    }

}
