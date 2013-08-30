package de.twenty11.skysail.server.core.restlet;

import java.util.List;

import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExtractingRequestFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingRequestHandlingFilter.class);
    
    @Override
    protected int doHandle(ListServerResource2<T> resource, Request request, ResponseWrapper<T> response) {
        List<T> data = resource.getData();
        response.getSkysailResponse().setData(data);
        return CONTINUE;
    }
}
