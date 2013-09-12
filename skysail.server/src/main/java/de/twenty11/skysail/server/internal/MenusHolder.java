package de.twenty11.skysail.server.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.MenuService;
import de.twenty11.skysail.server.core.osgi.internal.MenuLifecycle;
import de.twenty11.skysail.server.core.osgi.internal.MenuState;
import de.twenty11.skysail.server.core.osgi.internal.Trigger;
import de.twenty11.skysail.server.services.MenuEntry;

public class MenusHolder {

    private static Logger logger = LoggerFactory.getLogger(MenusHolder.class);

    private Map<MenuEntry, MenuLifecycle> menu = new HashMap<MenuEntry, MenuLifecycle>();
    private MenuLifecycle menuLifecycle;

    private final Configuration configuration;

    public MenusHolder(Configuration configuration)  {
        this.configuration = configuration;
    }

    public void add(List<MenuEntry> menuEntries) throws Exception {
        for (MenuEntry entry : menuEntries) {
            menu.put(entry, new MenuLifecycle(configuration));
        }
    }

    public void attach(MenuEntry menuEntry, MenuService menuService) throws Exception {
        if (menuService == null) {
            return;
        }
        MenuLifecycle menuEntryWithLifecycle = menu.get(menuEntry);
        menuEntryWithLifecycle.<MenuEntry> Fire(menuEntryWithLifecycle.getAttachTriggerWithMenuEntry(), menuEntry);
    }

    public void detach(MenuEntry menuEntry, MenuService menuService) throws Exception {
        if (menuService == null) {
            return;
        }
        menuService.removeApplicationFromMenu(menuEntry.getName(), menuEntry.getMenuIdentifier());
        logger.info("removed menu entry");
        menu.get(menuEntry).Fire(Trigger.DETACH);
    }

    public List<MenuEntry> getMenusInState(MenuState state) {
        List<MenuEntry> result = new ArrayList<MenuEntry>();
        for (Entry<MenuEntry, MenuLifecycle> entry : menu.entrySet()) {
            if (entry.getValue().getState().equals(state)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
