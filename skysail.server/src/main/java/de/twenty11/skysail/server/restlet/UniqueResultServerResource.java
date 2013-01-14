package de.twenty11.skysail.server.restlet;

import java.util.Set;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.responses.SuccessResponse;

public class UniqueResultServerResource <T> extends SkysailServerResource2<T> {

    protected Response<T> getEntity(T data) {
        try {
            RestletOsgiApplication app = (RestletOsgiApplication)getApplication();
            Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener().getMappings() : null;
            return new SuccessResponse<T>(data, getRequest(), mappings);
        } catch (Exception e) {
            //logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }

}
