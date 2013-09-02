package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.server.core.restlet.ExceptionCatchingRequestHandlingFilter;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailRequestHandlingFilter;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class FormDataExtractingRequestFilter<T> extends SkysailRequestHandlingFilter<T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingRequestHandlingFilter.class);

    @Override
    protected int doHandle(SkysailServerResource<T> resource, Request request, ResponseWrapper<T> response) {
        if (request == null || request.getResourceRef() == null) {
            response.setSkysailResponse(new FailureResponse<T>("request or resource reference was null"));
            return STOP;
        }
        Form form = request.getResourceRef().getQueryAsForm();
        T data = resource.getData(form);
        response.getSkysailResponse().setMessage(resource.getMessage("tobedone"));
        response.getSkysailResponse().setData(data);
        return CONTINUE;
    }
}
