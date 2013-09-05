//package de.twenty11.skysail.server.core.restlet.filter;
//
//import org.restlet.Request;
//import org.restlet.Response;
//
//import de.twenty11.skysail.common.responses.SkysailResponse;
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//
//@Deprecated
//public abstract class AbstractListResourceFilter<T> implements
//		ListResourceFilter<T> {
//
//	private volatile AbstractListResourceFilter<T> next;
//
//	public AbstractListResourceFilter() {
//	}
//
//	public AbstractListResourceFilter(AbstractListResourceFilter<T> next) {
//		this.next = next;
//	}
//
//	@Override
//	public FilterResult beforeHandle(ListServerResource<T> resource,
//			Request request, ResponseWrapper<T> response) {
//		return FilterResult.CONTINUE;
//	}
//
//	@Override
//	public FilterResult doHandle(ListServerResource<T> resource,
//			Request request, ResponseWrapper<T> response) {
//		AbstractListResourceFilter<T> next = getNext();
//		if (next != null) {
//			next.handle(resource, request, response);
//		}
//		return FilterResult.CONTINUE;
//	}
//
//	@Override
//	public void afterHandle(ListServerResource<T> resource, Request request,
//			ResponseWrapper<T> response) {
//	}
//
//	/**
//	 * Handles a call. Creates an empty {@link Response} object and then invokes
//	 * {@link #handle(Request, Response)}.
//	 * 
//	 * @param resource
//	 * 
//	 * @param request
//	 *            The request to handle.
//	 * @return The returned response.
//	 */
//	public final ResponseWrapper<T> handle(ListServerResource<T> resource,
//			Request request) {
//		ResponseWrapper<T> response = new ResponseWrapper<T>(
//				new SkysailResponse<T>());
//		handle(resource, request, response);
//		return response;
//	}
//
//	/**
//	 * Handles a call by first invoking the beforeHandle() method for
//	 * pre-filtering, then distributing the call to the next Restlet via the
//	 * doHandle() method. When the handling is completed, it finally invokes the
//	 * afterHandle() method for post-filtering.
//	 * 
//	 * @param resource
//	 * 
//	 * @param request
//	 *            The request to handle.
//	 * @param response
//	 *            The response to update.
//	 */
//	public final void handle(ListServerResource<T> resource, Request request,
//			ResponseWrapper<T> response) {
//		switch (beforeHandle(resource, request, response)) {
//		case CONTINUE:
//			switch (doHandle(resource, request, response)) {
//			case CONTINUE:
//				afterHandle(resource, request, response);
//				break;
//			default:
//				break;
//			}
//			break;
//		case SKIP:
//			afterHandle(resource, request, response);
//            break;
//		case STOP:
//            break;
//		default:
//			throw new IllegalStateException("result from beforeHandle was not in [CONTINUE,SKIP,STOP]");
//		}
//
//	}
//
//	public AbstractListResourceFilter<T> calling(
//			AbstractListResourceFilter<T> next) {
//		AbstractListResourceFilter<T> lastInChain = getLast();
//		lastInChain.setNext(next);
//		return this;
//	}
//
//	private AbstractListResourceFilter<T> getLast() {
//		AbstractListResourceFilter<T> result = this;
//		while (result.getNext() != null) {
//			result = result.getNext();
//		}
//		return result;
//	}
//
//	public AbstractListResourceFilter<T> getNext() {
//		return this.next;
//	}
//
//	public void setNext(AbstractListResourceFilter next) {
//		// if ((next != null) && (next.getContext() == null)) {
//		// next.setContext(getContext());
//		// }
//
//		this.next = next;
//	}
//
//}
