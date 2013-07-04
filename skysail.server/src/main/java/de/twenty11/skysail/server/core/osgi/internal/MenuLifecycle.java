package de.twenty11.skysail.server.core.osgi.internal;

import com.googlecode.stateless4j.StateMachine;
import com.googlecode.stateless4j.triggers.TriggerWithParameters1;

import de.twenty11.skysail.server.internal.AttachMenuAction;
import de.twenty11.skysail.server.internal.Configuration;
import de.twenty11.skysail.server.services.MenuEntry;

public class MenuLifecycle extends StateMachine<MenuState, Trigger> {

    private TriggerWithParameters1<MenuEntry, MenuState, Trigger> attachTriggerWithMenuEntry;

    public MenuLifecycle(Configuration configuration) throws Exception {
        
        super(MenuState.NEW);

        AttachMenuAction attachMenuAction2 = new AttachMenuAction(configuration);
        
        attachTriggerWithMenuEntry = SetTriggerParameters(Trigger.ATTACH, MenuEntry.class);
        
        // New --- (attach) ---> Attached
        Configure(MenuState.NEW).Permit(Trigger.ATTACH, MenuState.ATTACHED);
        
        // Attached --- (detach) ---> Detached
        Configure(MenuState.ATTACHED)
            .<MenuEntry> OnEntryFrom(attachTriggerWithMenuEntry, attachMenuAction2, MenuEntry.class)
            .Permit(Trigger.DETACH, MenuState.DETACHED);
        
        // Detached --- (attach) ---> Attached
        Configure(MenuState.DETACHED).Permit(Trigger.ATTACH, MenuState.ATTACHED);

    }
    
    public TriggerWithParameters1<MenuEntry, MenuState, Trigger> getAttachTriggerWithMenuEntry() {
        return attachTriggerWithMenuEntry;
    }

}
