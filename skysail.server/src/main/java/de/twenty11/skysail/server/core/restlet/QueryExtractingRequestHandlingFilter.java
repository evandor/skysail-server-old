package de.twenty11.skysail.server.core.restlet;

import org.restlet.Request;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryExtractingRequestHandlingFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(QueryExtractingRequestHandlingFilter.class);
    private Form form;

    public QueryExtractingRequestHandlingFilter() {
        // TODO Auto-generated constructor stub
    }

    public QueryExtractingRequestHandlingFilter(SkysailRequestHandlingFilter<T> next) {
        super(next);
    }

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        if (request != null && request.getOriginalRef() != null) {
            form = request.getOriginalRef().getQueryAsForm();
        }
        return CONTINUE;
    }
}
