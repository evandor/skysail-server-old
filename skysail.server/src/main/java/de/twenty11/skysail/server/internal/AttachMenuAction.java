package de.twenty11.skysail.server.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.stateless4j.delegates.Action2;
import com.googlecode.stateless4j.transitions.Transition;

import de.twenty11.skysail.server.core.MenuService;
import de.twenty11.skysail.server.core.osgi.internal.MenuState;
import de.twenty11.skysail.server.core.osgi.internal.Trigger;
import de.twenty11.skysail.server.services.MenuEntry;

public class AttachMenuAction implements Action2<MenuEntry, Transition<MenuState,Trigger>> {

    private static final Logger logger = LoggerFactory.getLogger(AttachMenuAction.class);
    
    private final Configuration configuration;

    public AttachMenuAction(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void doIt(MenuEntry menuEntry, Transition<MenuState,Trigger> transition) {
        
        MenuService menuService = configuration.getMenuService();
        if (menuService == null) {
            return;
        }
        menuService.addApplicationToMenu(menuEntry.getName(), menuEntry.getMenuIdentifier(), menuEntry.getLink());
        logger.info("added menu entry");
        
    };

}
