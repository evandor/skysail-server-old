package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource;

public interface EntityResourceFilter<T> {

    public FilterResult beforeHandle(UniqueResultServerResource<T> resource, Request request,
            ResponseWrapper<T> response);

    public FilterResult doHandle(UniqueResultServerResource<T> resource, Request request, ResponseWrapper<T> response);

    public void afterHandle(UniqueResultServerResource<T> resource, Request request, ResponseWrapper<T> response);

}
