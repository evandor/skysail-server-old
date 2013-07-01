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

    private Map<MenuEntry, MenuLifecycle> lifecycles = new HashMap<MenuEntry, MenuLifecycle>();

    public void add(List<MenuEntry> menuEntries) throws Exception {
        for (MenuEntry entry : menuEntries) {
            lifecycles.put(entry, new MenuLifecycle());
        }
    }

    public void attach(MenuEntry menuEntry, MenuService menuService) throws Exception {

        if (menuService == null) {
            return;
        }
        menuService.addApplicationToMenu(menuEntry.getName(), menuEntry.getMenuIdentifier(), menuEntry.getLink());
        logger.info("added menu entry");
        lifecycles.get(menuEntry).Fire(Trigger.ATTACH);
    }

    public void detach(MenuEntry menuEntry, MenuService menuService) throws Exception {
        if (menuService == null) {
            return;
        }
        menuService.removeApplicationFromMenu(menuEntry.getName(), menuEntry.getMenuIdentifier());
        logger.info("removed menu entry");
        lifecycles.get(menuEntry).Fire(Trigger.DETACH);
    }

    public List<MenuEntry> getMenusInState(MenuState state) {
        List<MenuEntry> result = new ArrayList<MenuEntry>();
        for (Entry<MenuEntry, MenuLifecycle> entry : lifecycles.entrySet()) {
            if (entry.getValue().getState().equals(state)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
