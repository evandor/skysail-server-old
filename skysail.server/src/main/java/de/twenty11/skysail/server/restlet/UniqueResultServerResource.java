package de.twenty11.skysail.server.restlet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.DetailsLinkProvider;
import de.twenty11.skysail.common.forms.ConstraintViolations;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;

public class UniqueResultServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    protected SkysailResponse<T> getEntity(T data) {
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

    protected SkysailResponse<T> addEntity(EntityManager em, T entity, ConstraintViolations<T> constraintViolations) {
        if (constraintViolations.size() > 0) {
            // if (constraintViolations.getMsg() != null) {
            logger.warn("contraint violations found on {}: {}", entity, constraintViolations);
            // return new FailureResponse<ConstraintViolations<T>>(constraintViolations);
            return new FailureResponse<T>(entity, constraintViolations);
        }
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
            em.close();
            return new SuccessResponse<T>();
        } catch (Exception e) {
            e.printStackTrace();
            return new FailureResponse<T>(e.getMessage());
        }

    }

}
