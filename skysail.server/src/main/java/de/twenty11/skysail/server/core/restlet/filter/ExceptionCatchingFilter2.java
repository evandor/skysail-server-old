package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class ExceptionCatchingFilter2<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingFilter2.class);

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setSkysailResponse(new FailureResponse<T>(e));
        }
        return FilterResult.CONTINUE;
        // return super.doHandle(resource, request, response);
    }

}
