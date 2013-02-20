package de.twenty11.skysail.server.restlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.twenty11.skysail.common.DetailsLinkProvider;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.responses.SuccessResponse;

public class UniqueResultServerResource<T> extends SkysailServerResource2<T> {

    protected Response<T> getEntity(T data) {
        try {

            if (data instanceof DetailsLinkProvider) {
                Map<String, String> links = new HashMap<String, String>();
                DetailsLinkProvider dlp = (DetailsLinkProvider) data;
                for (Entry<String, String> linkEntry : dlp.getLinkMap().entrySet()) {
                    if (getReference() != null) { // e.g. during testing
                        links.put(linkEntry.getKey(), getReference().toString() + linkEntry.getValue());
                    }
                }
                dlp.setLinks(links);
            }

            SkysailApplication app = (SkysailApplication) getApplication();
            Set<String> mappings = app.getUrlMappingServiceListener() != null ? app.getUrlMappingServiceListener()
                    .getMappings() : null;
            return new SuccessResponse<T>(data, getRequest(), mappings);
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }

}
