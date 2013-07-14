package de.twenty11.skysail.server.um.resources;

import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.AddServerResource2;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailRole;

public class AddRoleResource extends AddServerResource2<SkysailRole> {

    @Override
    public FormResponse<SkysailRole> createForm() {
        return new FormResponse<SkysailRole>(new SkysailRole(), "../roles/");
    }

    @Override
    public SkysailRole getData(Form form) {
        return new SkysailRole(form.getFirstValue("name"));
    }

    @Override
    public SkysailResponse<?> addEntity(SkysailRole entity) {
        UserManagementApplication app = (UserManagementApplication) getApplication();
        app.getRoleRepository().add(entity);
        return new RolesResource().getEntities();
    }

}
