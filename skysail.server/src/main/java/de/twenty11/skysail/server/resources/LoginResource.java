package de.twenty11.skysail.server.resources;

import org.restlet.data.Form;
import org.restlet.resource.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.core.restlet.AddServerResource2;
import de.twenty11.skysail.server.domain.Credentials;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.security.AuthenticationService;

// TODO should extends ServerResource
public class LoginResource extends AddServerResource2<Credentials> {

    private static Logger logger = LoggerFactory.getLogger(LoginResource.class);

    @Override
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(new Credentials(), "login");
    }

    @Override
    public Credentials getData(Form form) {
        return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Post("x-www-form-urlencoded:html|json")
    public SkysailResponse<?> addFromForm(Form form) {
        // Credentials entity = getData(form);
        // Set<ConstraintViolation<Credentials>> violations = validate(entity);
        // if (violations.size() > 0) {
        // return new ConstraintViolationsResponse(entity, null, violations);
        // }
        return new SuccessResponse(); // Entity(entity);
    }

    @Override
    public SkysailResponse<?> addEntity(Credentials entity) {
        AuthenticationService authService = ((DefaultSkysailApplication) getApplication()).getAuthenticationService();
        try {
            // authService.login(entity.getUsername(), entity.getPassword());
        } catch (Exception e) {
            return new FailureResponse(e);
            // return new DefaultResource().getApplications();
        }
        return new DefaultResource().getApplications();
    }

}
