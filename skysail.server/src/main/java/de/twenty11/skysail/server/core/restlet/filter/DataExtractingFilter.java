package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class DataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingFilter.class);

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T data = (T) resource.getData();
        response.getSkysailResponse().setMessage(resource.getMessage("tobedone"));
        response.getSkysailResponse().setData(data);
        return FilterResult.CONTINUE;
    }

}
