package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public interface ResourceFilter<R extends SkysailServerResource<T>, T> {

    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response);

}
