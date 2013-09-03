package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResourceFilter;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class QueryExtractingFilter<T> extends ResourceFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(QueryExtractingFilter.class);
    private Form form;

    @Override
    protected int beforeHandle(Resource resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#beforeHandle", this.getClass().getSimpleName());
        return CONTINUE;
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        logger.info("entering {}#doHandle", this.getClass().getSimpleName());

        if (request != null && request.getOriginalRef() != null) {
            form = request.getOriginalRef().getQueryAsForm();
        }
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Resource resource, Request request, ResponseWrapper response) {
        logger.debug("entering {}#afterHandle", this.getClass().getSimpleName());
    }

}
