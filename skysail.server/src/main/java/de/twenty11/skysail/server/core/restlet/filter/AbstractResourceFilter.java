package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public abstract class AbstractResourceFilter<R extends SkysailServerResource<T>, T> implements ResourceFilter<R, T> {

    private volatile AbstractResourceFilter<R, T> next;

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        AbstractResourceFilter<R, T> next = getNext();
        if (next != null) {
            next.handle(resource, request, response);
        }
        return FilterResult.CONTINUE;

    }

    public final void handle(R resource, Request request, ResponseWrapper<T> response) {
        // switch (beforeHandle(resource, request, response)) {
        // case CONTINUE:
        switch (doHandle(resource, request, response)) {
        case CONTINUE:
            // afterHandle(resource, request, response);
            break;
        default:
            break;
        }
        // break;

        // default:
        // break;
        // }

    }

    public final ResponseWrapper<T> handle(R resource, Request request) {
        ResponseWrapper<T> response = new ResponseWrapper<T>(new SkysailResponse<T>());
        handle(resource, request, response);
        return response;
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

}
