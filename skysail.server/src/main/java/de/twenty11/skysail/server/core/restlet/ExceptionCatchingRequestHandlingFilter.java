package de.twenty11.skysail.server.core.restlet;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;

public class ExceptionCatchingRequestHandlingFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingRequestHandlingFilter.class);

    // public ExceptionCatchingRequestHandlingFilter(SkysailRequestHandlingFilter<T> next) {
    // super(next);
    // }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        try {
            super.doHandle(resource, request, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.setSkysailResponse(new FailureResponse<T>(e));
        }
        return CONTINUE;
    }
}
