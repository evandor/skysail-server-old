package de.twenty11.skysail.server.menu;

import java.util.List;

import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.menu.domain.Menu;
import de.twenty11.skysail.server.menu.resources.MenusResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.services.MenuService;

public class MenuApplication extends SkysailApplication implements ApplicationProvider, MenuService {

    private static final Logger logger = LoggerFactory.getLogger(MenuApplication.class);

    public static final String MAIN_MENU_IDENTIFIER = "main";
    public static final String NAV_MENU_IDENTIFIER = "nav";

    private Menu root;

    private Menu mainMenu;

    private Menu navMenu;

    public MenuApplication() {
        super("menu");
        setName("menu");
        setDescription("provides the menus");
        setOwner("twenty11");

        root = new Menu();
        mainMenu = new Menu(root, MAIN_MENU_IDENTIFIER, "#");
        navMenu = new Menu(root, NAV_MENU_IDENTIFIER, "#");
    }

    @Override
    protected void attach() {

        router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
        router.setRoutingMode(Router.MODE_LAST_MATCH);

        router.attach(new RouteBuilder("", MenusResource.class).setText("menu"));
        router.attach(new RouteBuilder("/", MenusResource.class).setText("menu"));
        router.attach(new RouteBuilder("/{path}", MenusResource.class).setText("menupath"));
    }

    @Override
    public void addApplicationToMenu(String appIdentifier, String menuIdentifier, String link) {
        logger.debug("application {} is assigned to menu {}", appIdentifier, menuIdentifier);
        new Menu(mainMenu, appIdentifier, link);
    }

    @Override
    public void removeApplicationFromMenu(String appIdentifier, String menuIdentifier) {
        logger.debug("application {} is detached from menu {}", appIdentifier, menuIdentifier);
        mainMenu.remove(appIdentifier);
    }

    public List<Menu> getMenus(String path) {
        return root.getChildren(path);
    }

}
