package de.twenty11.skysail.server.resources;

import java.util.List;

import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;

/**
 * Default resource, attached to path "/". Noop for now.
 * 
 */
public class DefaultResource extends ListServerResource<String> {

    private static Logger logger = LoggerFactory.getLogger(DefaultResource.class);

    public DefaultResource() {
        logger.debug("instanciation of DefaultResource");
        // setName("Skysail Server Available Applications Resource");
        // setDescription("The resource containing the list of available applications");
    }

    @Override
    public List<String> getData() {
        return null;
    }

    @Override
    public List<String> getData(Form form) {
        return null;
    }

    @Override
    public String getMessage(String key) {
        return "Welcome";
    }

    @Override
    public SkysailResponse<?> addEntity(List<String> entity) {
        return null;
    }

}
