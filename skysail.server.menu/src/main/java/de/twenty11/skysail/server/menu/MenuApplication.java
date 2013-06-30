package de.twenty11.skysail.server.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restlet.routing.Router;
import org.restlet.routing.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.MenuService;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.menu.domain.Menu;
import de.twenty11.skysail.server.menu.resources.MenusResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;

public class MenuApplication extends SkysailApplication implements ApplicationProvider, MenuService {

	private static final Logger logger = LoggerFactory.getLogger(MenuApplication.class);
	
	public static final String MAIN_MENU_IDENTIFIER = "/main";
	public static final String NAV_MENU_IDENTIFIER = "/nav";

	//private Map<String, Menu> menus = new HashMap<String,Menu>();

	private Menu root;

	public MenuApplication() {
		setName("menu");
		setDescription("provides the menus");
		setOwner("twenty11");
		
		root = new Menu();
		new Menu(root, MAIN_MENU_IDENTIFIER);
		new Menu(root, NAV_MENU_IDENTIFIER);
		
//		menus.put(MAIN_MENU_IDENTIFIER, new Menu(MAIN_MENU_IDENTIFIER));
//		menus.put(NAV_MENU_IDENTIFIER, new Menu(NAV_MENU_IDENTIFIER));
	}

	@Override
	protected void attach() {

		router.setDefaultMatchingMode(Template.MODE_STARTS_WITH);
        router.setRoutingMode(Router.MODE_LAST_MATCH);

		router.attach(new RouteBuilder("", MenusResource.class).setText("menu"));
		router.attach(new RouteBuilder("/{path}", MenusResource.class).setText("menupath"));
	}

	@Override
	public void addApplicationToMenu(String appIdentifier, String menuIdentifier) {
		logger.debug("application {} is assigned to menu {}", appIdentifier, menuIdentifier);
//		if (menus.containsKey(menuIdentifier)) {
//			new Menu(menus.get(menuIdentifier), appIdentifier);
//		} else {
//			logger.warn("menuIdentifier {} is unknown...", menuIdentifier);
//		}
 	}
	
	public List<Menu> getMenus(String path) {
		return root.getChildren(path);
		
//		if (path == null) {
//			Collection<Menu> values = menus.values();
//			List<Menu> result = new ArrayList<Menu>();
//			for (Menu menu : values) {
//				result.add(menu);
//			}
//			return result;
//		}
//		return Collections.emptyList();
	}

}
