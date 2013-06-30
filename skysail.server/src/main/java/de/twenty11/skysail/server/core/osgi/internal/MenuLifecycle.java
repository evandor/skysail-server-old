package de.twenty11.skysail.server.core.osgi.internal;

import com.googlecode.stateless4j.StateMachine;

public class MenuLifecycle extends StateMachine<MenuState, Trigger> {

    public MenuLifecycle() throws Exception {
        super(MenuState.NEW);

        // New --- (attach) ---> Attached
        Configure(MenuState.NEW).Permit(Trigger.ATTACH, MenuState.ATTACHED);
        
        // Attached --- (detach) ---> Detached
        Configure(MenuState.ATTACHED).Permit(Trigger.DETACH, MenuState.DETACHED);
        
        // Detached --- (attach) ---> Attached
        Configure(MenuState.DETACHED).Permit(Trigger.ATTACH, MenuState.ATTACHED);
    }

}
