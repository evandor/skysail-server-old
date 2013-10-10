package de.twenty11.skysail.server.core.restlet.filter;

import org.restlet.Request;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;

public class FormDataExtractingFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(ExceptionCatchingFilter.class);

    @Override
    public FilterResult doHandle(R resource, Request request, ResponseWrapper<T> response) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        if (request == null || request.getResourceRef() == null) {
            response.setSkysailResponse(new FailureResponse<T>("request or resource reference was null"));
            return FilterResult.STOP;
        }
        Form form = (Form) request.getAttributes().get(EntityServerResource.SKYSAIL_SERVER_RESTLET_FORM);
        T data = (T) resource.getData(form);
        response.getSkysailResponse().setMessage(resource.getMessage("tobedone"));
        response.getSkysailResponse().setData(data);

        super.doHandle(resource, request, response);

        return FilterResult.CONTINUE;
    }

}
