package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class QueryExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(QueryExtractingFilter.class);
    private Form form;

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.info("entering {}#doHandle", this.getClass().getSimpleName());

        if (request != null && request.getOriginalRef() != null) {
            form = request.getOriginalRef().getQueryAsForm();
        }
        return FilterResult.CONTINUE;
    }

}
