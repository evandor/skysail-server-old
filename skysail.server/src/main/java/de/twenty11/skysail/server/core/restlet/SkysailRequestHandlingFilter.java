package de.twenty11.skysail.server.core.restlet;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.responses.SkysailResponse;

public abstract class SkysailRequestHandlingFilter<T> {

    public static final int CONTINUE = 0;
    private volatile SkysailRequestHandlingFilter<T> next;

    public SkysailRequestHandlingFilter() {
    }

    public SkysailRequestHandlingFilter(SkysailRequestHandlingFilter<T> next) {
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

    // public final ResponseWrapper<List<T>> handleList(SkysailServerResource2<List<T>> resource, Request request) {
    // ResponseWrapper<List<T>> response = new ResponseWrapper<List<T>>(new SkysailResponse<List<T>>());
    // handleList(resource, request, response);
    // return response;
    // }

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
        final int result = CONTINUE;

        if (getNext() != null) {
            getNext().doHandle(resource, request, response);

            // // Re-associate the response to the current thread
            // Response.setCurrent(response);
            //
            // // Associate the context to the current thread
            // if (getContext() != null) {
            // Context.setCurrent(getContext());
            // }
        } else {
            // response.setStatus(Status.SERVER_ERROR_INTERNAL);
            // getLogger()
            // .warning(
            // "The filter "
            // + getName()
            // + " was executed without a next Restlet attached to it.");
        }

        return result;
    }

    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
    }

    protected SkysailRequestHandlingFilter<T> calling(SkysailRequestHandlingFilter<T> next) {
        setNext(next);
        return this;
    }

    public SkysailRequestHandlingFilter getNext() {
        return this.next;
    }

    public void setNext(SkysailRequestHandlingFilter next) {
        // if ((next != null) && (next.getContext() == null)) {
        // next.setContext(getContext());
        // }

        this.next = next;
    }
}
