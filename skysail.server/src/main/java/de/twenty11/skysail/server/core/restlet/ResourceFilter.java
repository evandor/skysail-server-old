package de.twenty11.skysail.server.core.restlet;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.responses.SkysailResponse;

public abstract class ResourceFilter<T> {

    /**
     * // TODO Indicates that the request processing should continue normally. If returned from the
     * {@link #beforeHandle(Request, Response)} method, the filter then invokes the {@link #doHandle(Request, Response)}
     * method. If returned from the {@link #doHandle(Request, Response)} method, the filter then invokes the
     * {@link #afterHandle(Request, Response)} method.
     */
    public static final int CONTINUE = 0;

    /**
     * // TODO Indicates that after the {@link #beforeHandle(Request, Response)} method, the request processing should
     * skip the {@link #doHandle(Request, Response)} method to continue with the {@link #afterHandle(Request, Response)}
     * method.
     */
    public static final int SKIP = 1;

    /**
     * // TODO Indicates that the request processing should stop and return the current response from the filter.
     */
    public static final int STOP = 2;

    private volatile ResourceFilter<T> next;

    public ResourceFilter() {
    }

    public ResourceFilter(ResourceFilter<T> next) {
        this.next = next;
    }

    protected int beforeHandle(Resource resource, Request request, ResponseWrapper<T> response) {
        return CONTINUE;
    }

    /**
     * Handles a call. Creates an empty {@link Response} object and then invokes {@link #handle(Request, Response)}.
     * 
     * @param resource
     * 
     * @param request
     *            The request to handle.
     * @return The returned response.
     */
    public final ResponseWrapper<T> handle(SkysailServerResource<T> resource, Request request) {
        ResponseWrapper<T> response = new ResponseWrapper<T>(new SkysailResponse<T>());
        handle(resource, request, response);
        return response;
    }

    /**
     * Handles a call by first invoking the beforeHandle() method for pre-filtering, then distributing the call to the
     * next Restlet via the doHandle() method. When the handling is completed, it finally invokes the afterHandle()
     * method for post-filtering.
     * 
     * @param resource
     * 
     * @param request
     *            The request to handle.
     * @param response
     *            The response to update.
     */
    public final void handle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
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

        default:
            break;
        }
    }

    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        ResourceFilter<T> next = getNext();
        if (next != null) {
            next.handle(resource, request, response);
        }
        return CONTINUE;
    }

    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
    }

    protected ResourceFilter<T> calling(ResourceFilter<T> next) {
        ResourceFilter<T> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    public ResourceFilter<T> getNext() {
        return this.next;
    }

    public void setNext(ResourceFilter next) {
        this.next = next;
    }

    private ResourceFilter<T> getLast() {
        ResourceFilter<T> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
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
