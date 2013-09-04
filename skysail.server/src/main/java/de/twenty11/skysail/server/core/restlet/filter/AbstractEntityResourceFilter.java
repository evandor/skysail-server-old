package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource;

@Deprecated
public abstract class AbstractEntityResourceFilter<T> implements EntityResourceFilter<T> {

    private volatile AbstractEntityResourceFilter<T> next;

    public AbstractEntityResourceFilter() {
    }

    public AbstractEntityResourceFilter(AbstractEntityResourceFilter<T> next) {
        this.next = next;
    }

    @Override
    public FilterResult beforeHandle(UniqueResultServerResource<T> resource, Request request,
            ResponseWrapper<T> response) {
        return FilterResult.CONTINUE;
    }

    @Override
    public FilterResult doHandle(UniqueResultServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        AbstractEntityResourceFilter<T> next = getNext();
        if (next != null) {
            next.handle(resource, request, response);
        }
        return FilterResult.CONTINUE;
    }

    @Override
    public void afterHandle(UniqueResultServerResource<T> resource, Request request, ResponseWrapper<T> response) {
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
    public final ResponseWrapper<T> handle(UniqueResultServerResource<T> resource, Request request) {
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
    public final void handle(UniqueResultServerResource<T> resource, Request request, ResponseWrapper<T> response) {
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

    public AbstractEntityResourceFilter<T> calling(AbstractEntityResourceFilter<T> next) {
        AbstractEntityResourceFilter<T> lastInChain = getLast();
        lastInChain.setNext(next);
        return this;
    }

    private AbstractEntityResourceFilter<T> getLast() {
        AbstractEntityResourceFilter<T> result = this;
        while (result.getNext() != null) {
            result = result.getNext();
        }
        return result;
    }

    public AbstractEntityResourceFilter<T> getNext() {
        return this.next;
    }

    public void setNext(AbstractEntityResourceFilter next) {
        // if ((next != null) && (next.getContext() == null)) {
        // next.setContext(getContext());
        // }

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
