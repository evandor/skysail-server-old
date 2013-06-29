package de.twenty11.skysail.server.menu;

import java.util.ArrayList;
import java.util.List;

import de.twenty11.skysail.server.menu.domain.Menu;
import de.twenty11.skysail.server.menu.osgi.services.MenuService;
import de.twenty11.skysail.server.restlet.SkysailApplication;

public class MenuApplication extends SkysailApplication implements MenuService {

	public static final String MAIN_MENU_IDENTIFIER = "main";
	public static final String NAV_MENU_IDENTIFIER = "nav";

	private List<Menu> rootLevelMenus = new ArrayList<Menu>();

	public MenuApplication() {
		setName("skysailMenu");
		setDescription("provides the menus");
		setOwner("twenty11");
		rootLevelMenus.add(new Menu(MAIN_MENU_IDENTIFIER));
		rootLevelMenus.add(new Menu(NAV_MENU_IDENTIFIER));
	}

	@Override
	protected void attach() {
		// router.attach(")
	}

	@Override
	public void addApplicationToMenu(String appIdentifier, String menuIdentifier) {
		// TODO Auto-generated method stub
		
	}

}
