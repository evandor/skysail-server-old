package de.twenty11.skysail.server.um.resources;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource2;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Presentation(preferred = PresentationStyle.LIST2)
public class UserResource extends UniqueResultServerResource2<SkysailUser> {

    private String username;

    @Override
    protected void doInit() throws ResourceException {
        username = (String) getRequest().getAttributes().get("username");
    }

    @Override
    protected SkysailUser getData() {
        UserManagementApplication app = (UserManagementApplication) getApplication();
        return app.getUserRepository().getByName(username);
    }

    @Override
    public SkysailUser getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(SkysailUser entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
