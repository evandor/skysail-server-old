package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.routing.Filter;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

/**
 * The abstract base class for Skysail Resource Filters.
 * 
 * The approach is similar to what happens in the Restlet {@link Filter} system, but happens after the resource (which
 * handles the request) has been found. Filtering therefore is based on a {@link SkysailServerResource}, the incoming
 * {@link Request} and the outgoing (wrapped) response.
 * 
 * 
 * @param <R> a {@link SkysailServerResource} with T as Type Parameter
 * @param <T> a type representing an Entity
 */
public abstract class AbstractResourceFilter<R extends SkysailServerResource<T>, T> {

    private AbstractResourceFilter<R, T> next;

    /**
     * The entry point when using Resource Filters.
     * 
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param request
     *            the request to handle
     * @return the result of the processing
     */
    public final ResponseWrapper<T> handle(R resource, Request request) {
        ResponseWrapper<T> response = new ResponseWrapper<T>(new SkysailResponse<T>());
        handle(resource, request, response);
        return response;
    }

    /**
     * pre-processing logic, called before the control is passed to the doHandle Method.
     * 
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param request
     *            the request to handle
     * @param response
     *            the response to update
     * @return the {@link FilterResult} of the processing, indicating whether to Continue, Skip or Stop.
     */
    protected FilterResult beforeHandle(R resource, Request request, ResponseWrapper<T> response) {
        return FilterResult.CONTINUE;
    }

    /**
     * the main processing logic
     * 
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param request
     *            the request to handle
     * @param response
     *            the response to update
     * @return the {@link FilterResult} of the processing, indicating whether to Continue, Skip or Stop.
     */
    protected FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        AbstractResourceFilter<R, T> next = getNext();
        if (next != null) {
            next.handle(resource, request, response);
        }
        return FilterResult.CONTINUE;
    }

    /**
     * post-processing logic, called before the control is passed to the doHandle Method.
     * 
     * @param resource
     *            a {@link SkysailServerResource} object
     * @param request
     *            the request to handle
     * @param response
     *            the response to update
     */
    protected void afterHandle(R resource, Request request, ResponseWrapper<T> response) {
        // default implementation doesn't do anything
    }

    private final void handle(R resource, Request request, ResponseWrapper<T> response) {
        switch (beforeHandle(resource, request, response)) {
        case CONTINUE:
            switch (doHandle(resource, request, response)) {
            case CONTINUE:
                afterHandle(resource, request, response);
                break;
            default:
                break;
            }
            break;
        case SKIP:
            afterHandle(resource, request, response);
            break;
        case STOP:
            break;
        default:
            throw new IllegalStateException("result from beforeHandle was not in [CONTINUE,SKIP,STOP]");
        }
    }

    public AbstractResourceFilter<R, T> calling(AbstractResourceFilter<R, T> next) {
        AbstractResourceFilter<R, T> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    private AbstractResourceFilter<R, T> getNext() {
        return next;
    }

    private AbstractResourceFilter<R, T> getLast() {
        AbstractResourceFilter<R, T> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
    }

    private void setNext(AbstractResourceFilter<R, T> next) {
        this.next = next;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        if (getNext() != null) {
            sb.append(" -> ").append(getNext().toString());
        }
        return sb.toString();
    }
}
