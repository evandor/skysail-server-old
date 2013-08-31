package de.twenty11.skysail.server.core.restlet;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExtractingRequestFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingRequestHandlingFilter.class);

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        T data = resource.getData();
        response.getSkysailResponse().setData(data);
        return CONTINUE;
    }
}
