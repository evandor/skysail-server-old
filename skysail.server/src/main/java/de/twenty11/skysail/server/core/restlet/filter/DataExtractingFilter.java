package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResourceFilter;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class DataExtractingFilter<T> extends ResourceFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingFilter.class);

    @Override
    protected int beforeHandle(Resource resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#beforeHandle", this.getClass().getSimpleName());
        return CONTINUE;
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        T data = resource.getData();
        response.getSkysailResponse().setMessage(resource.getMessage("tobedone"));
        response.getSkysailResponse().setData(data);
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
        logger.debug("entering {}#afterHandle", this.getClass().getSimpleName());
    }

}
