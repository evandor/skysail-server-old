//package de.twenty11.skysail.server.core.restlet.filter;
//
//import org.restlet.Request;
//import org.restlet.routing.Filter;
//
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
//
///**
// * A Filter for restlet resources.
// * 
// * The approach is similar to what happens in the Restlet {@link Filter} system, but happens after
// * the resource (which handles the request) has been found. Filtering therefore is based on a 
// * {@link SkysailServerResource}, the incoming {@link Request} and the outgoing (wrapped) response. 
// * 
// * This interface is implemented by {@link AbstractResourceFilter}, which provides the logic of 
// * preprocessing, actual processing and postprocessing.
// *
// * @param <R>
// * @param <T>
// */
//public interface ResourceFilter<R extends SkysailServerResource<T>, T> {
//
//    FilterResult beforeHandle(R resource, Request request, ResponseWrapper<T> response);
//
//    FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response);
//
//    void afterHandle(R resource, Request request, ResponseWrapper<T> response);
//
//}
