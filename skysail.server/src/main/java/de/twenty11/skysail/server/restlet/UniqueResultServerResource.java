package de.twenty11.skysail.server.restlet;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.GenericBootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.ConstraintViolationsResponse;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource2;

/**
 * @deprecated use UniqueResultServerResource2
 */
@Deprecated
public class UniqueResultServerResource<T> extends SkysailServerResource2<T> {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Validator validator;

    public UniqueResultServerResource() {
        GenericBootstrap validationProvider = Validation.byDefaultProvider();

        javax.validation.Configuration<?> config = validationProvider.providerResolver(new OSGiServiceDiscoverer())
                .configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        validator = factory.getValidator();
    }

    protected SkysailResponse<T> getEntity(T data) {
        try {

            // if (data instanceof DetailsLinkProvider) {
            // Map<String, String> links = new HashMap<String, String>();
            // DetailsLinkProvider dlp = (DetailsLinkProvider) data;
            // for (Entry<String, String> linkEntry : dlp.getLinkMap().entrySet()) {
            // if (getReference() != null) { // e.g. during testing
            // links.put(linkEntry.getKey(), getReference().toString() + linkEntry.getValue());
            // }
            // }
            // dlp.setLinks(links);
            // }

            SkysailApplication app = (SkysailApplication) getApplication();
            return new SuccessResponse<T>(data);
        } catch (Exception e) {
            // logger.error(e.getMessage(), e);
            return new FailureResponse<T>(e);
        }
    }

    protected SkysailResponse<T> addEntity(EntityManager em, T entity, Set<ConstraintViolation<T>> violations) {
        if (violations.size() > 0) {
            // if (constraintViolations.getMsg() != null) {
            logger.warn("contraint violations found on {}: {}", entity, violations);
            // return new FailureResponse<ConstraintViolations<T>>(constraintViolations);
            return new ConstraintViolationsResponse<T>(entity, null, violations);
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

    public Validator getValidator() {
        return validator;
    }

}
