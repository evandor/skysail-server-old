//package de.twenty11.skysail.server.camel;
//
//import org.apache.camel.builder.RouteBuilder;
//
//public class ReportIncidentRoutes extends RouteBuilder {
//
//	@Override
//	public void configure() throws Exception {
//		from("direct:start")
//        // to is the destination we send the message to our velocity endpoint
//        // where we transform the mail body
//        .to("velocity:MailBody.vm");
//		
//	}
//
//	
//
//}
