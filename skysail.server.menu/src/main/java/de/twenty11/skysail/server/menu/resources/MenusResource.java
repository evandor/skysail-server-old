package de.twenty11.skysail.server.menu.resources;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.menu.MenuApplication;
import de.twenty11.skysail.server.menu.domain.Menu;

public class MenusResource extends ListServerResource2<Menu> {

    private MenuApplication menuApplication;
    private String path;

    @Override
    protected void doInit() throws ResourceException {
        menuApplication = (MenuApplication) getApplication();
        Object pathAsObject = getRequest().getAttributes().get("path");
        path = (pathAsObject != null) ? "/" + (String) pathAsObject : "/";
    }

    @Override
    protected List<Menu> getData() {
        return menuApplication.getMenus(path);
    }

    @Override
    public Menu getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(Menu entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
