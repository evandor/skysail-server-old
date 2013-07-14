package de.twenty11.skysail.server.um.resources;

import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.AddServerResource2;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailUser;

public class AddUserResource extends AddServerResource2<SkysailUser> {

    @Override
    public FormResponse<SkysailUser> createForm() {
        return new FormResponse<SkysailUser>(new SkysailUser(), "../users/");
    }

    @Override
    public SkysailUser getData(Form form) {
        return new SkysailUser(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Override
    public SkysailResponse<?> addEntity(SkysailUser entity) {
        UserManagementApplication app = (UserManagementApplication) getApplication();
        app.getUserRepository().add(entity);
        return new UsersResource().getEntities();
    }

}
