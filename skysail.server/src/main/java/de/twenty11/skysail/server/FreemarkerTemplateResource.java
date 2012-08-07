//package de.twenty11.skysail.server;
//
//import de.twenty11.skysail.common.SkysailData;
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.server.restlet.SkysailServerResource;
//
//public class FreemarkerTemplateResource extends SkysailServerResource<SkysailData> {
//
//	public FreemarkerTemplateResource() {
//		super(null);
//	}
//
//	@Override
//	public void setResponseDetails(SkysailResponse<SkysailData> response) {
//        response.setMessage(getMessage());
//        response.setTotalResults(0);
//        response.setPage(1);
//        response.setPageSize(0);
//        //response.setOrigRequest(getRequest().getOriginalRef().toUrl());
//        response.setRequest(getRequest().getOriginalRef().toString());
//        response.setParent(getParent());
//        response.setContextPath("/rest/");
//        response.setFilter("");
//	}
//
//	@Override
//	public SkysailData getFilteredData() {
//		return getSkysailData();
//	}
//
//}
