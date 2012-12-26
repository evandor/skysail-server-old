///**
// *  Copyright 2011 Carsten Gräf
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// * 
// */
//package de.twenty11.skysail.server.communication;
//
//import java.io.IOException;
//
//import org.restlet.data.MediaType;
//import org.restlet.ext.jackson.JacksonRepresentation;
//import org.restlet.representation.Representation;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.common.SkysailData;
//import de.twenty11.skysail.common.responses.SkysailFailureResponse;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//
///**
// * @author Graef
// * 
// */
//public class CommunicationUtils {
//
//    private static final String SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL = "skysail.server:menu.ftl";
//
//    private static final Logger  logger = LoggerFactory.getLogger(CommunicationUtils.class);
//
//    private String template = SKYSAIL_SERVER_RESTLETOSGI_MENU_FTL;
//    
//    public static Representation createErrorResponse(final Exception e, final org.slf4j.Logger logger, MediaType mediaType) {
//        //logger.info("creating error representation for variant " + variant);
//        SkysailResponse<SkysailData> res = new SkysailFailureResponse(e);
//        logger.error(e.getMessage(), e);
//        if (mediaType.equals(MediaType.APPLICATION_JSON)) {
//            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
//        } else if (mediaType.equals(MediaType.TEXT_HTML)) {
////            Template ftlTemplate = getFtlTemplate("skysail.server:errormessage.ftl");
////            return new TemplateRepresentation(ftlTemplate, res, MediaType.TEXT_HTML);
//            throw new RuntimeException("media type '" + mediaType + "' not supported");
//        } else if (mediaType.equals(MediaType.TEXT_XML)) {
//            return new JacksonRepresentation<SkysailResponse<SkysailData>>(res);
//        } else {
//            throw new RuntimeException("media type '" + mediaType + "' not supported");
//        }
//    }
// }
