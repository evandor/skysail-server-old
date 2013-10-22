package de.twenty11.skysail.server.resources;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.domain.Credentials;

public class LoginResource extends EntityServerResource<Credentials> {

    @Get("htmlform")
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(new Credentials(), "login");
    }

    @Override
    public Credentials getData(Form form) {
        return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Post("x-www-form-urlencoded:html|json")
    public SkysailResponse<?> addFromForm(Form form) {
        return new SuccessResponse(); // Entity(entity);
    }

    @Override
    protected void doInit() throws ResourceException {
    }

    @Override
    public Credentials getData() {
        return null;
    }

    @Override
    public String getMessage(String key) {
        return "hi";
    }

    @Override
    public SkysailResponse<?> addEntity(Credentials entity) {
        return null;
    }

    @Override
    public SkysailResponse<?> updateEntity(Credentials entity) {
        return null;
    }

}
