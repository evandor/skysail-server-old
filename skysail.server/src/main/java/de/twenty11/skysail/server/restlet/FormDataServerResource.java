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
//
//package de.twenty11.skysail.server.restlet;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.common.forms.FormBuilder;
//import de.twenty11.skysail.common.forms.FormData;
//
///**
// * An abstract class dealing with common functionality for a skysail server resource which is backed-up by a GridData
// * object.
// * 
// * @author carsten
// * 
// */
//public abstract class FormDataServerResource extends SkysailServerResource<FormData> {
//
//    /** slf4j based logger implementation. */
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * @param data
//     *            the skysail data backing up the resource.
//     */
//    public FormDataServerResource(final FormData data) {
//        super(null);
//    }
//
//    public abstract FormData fillForm(FormData formData);
//
//    public abstract void configureForm(FormBuilder builder);
//
//    @Override
//    public FormData getFilteredData() {
//
//        // define the columns for the result (for grids and assign to grid)
//        FormBuilder formBuilder = new FormBuilder() {
//            @Override
//            public void configure() {
//                configureForm(this);
//            }
//        };
//
//        FormData formData = new FormData(formBuilder);
//        // if (getSkysailData() instanceof FormData) {
//        // ((FormData)getSkysailData()).setFormBuilder(formBuilder);
//        // }
//
//        return fillForm(formData);
//    }
//
//    // @Override
//    // public void setResponseDetails(SkysailResponse<FormData> response, MediaType media) {
//    // response.setMessage(getMessage());
//    // //response.setTotalResults(1);
//    // //response.setPage(getCurrentPage());
//    // //response.setPageSize(getPageSize());
//    // //response.setOrigRequest(getRequest().getOriginalRef().toUrl());
//    // response.setRequest(getRequest().getOriginalRef().toString());
//    // response.setParent(getParent());
//    // response.setContextPath("/rest/");
//    // //response.setFilter(getFilter() != null ? getFilter().toString() : "");
//    // //response.setSortingRepresentation(getSorting());
//    // if (getQuery() != null && getQuery().getNames().contains("debug")) {
//    // response.setDebug(true);
//    // }
//    // }
//
//}
