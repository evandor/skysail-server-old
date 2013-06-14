package de.twenty11.skysail.server.core.osgi.internal;

import com.googlecode.stateless4j.StateMachine;

public class ApplicationLifecycle extends StateMachine<ApplicationState, Trigger> {

    public ApplicationLifecycle() throws Exception {
        super(ApplicationState.NEW);

        // New --- (attach) ---> Attached
        Configure(ApplicationState.NEW).Permit(Trigger.ATTACH, ApplicationState.ATTACHED);
        
        // Attached --- (detach) ---> Detached
        Configure(ApplicationState.ATTACHED).Permit(Trigger.DETACH, ApplicationState.DETACHED);
        
        // Detached --- (attach) ---> Attached
        Configure(ApplicationState.DETACHED).Permit(Trigger.ATTACH, ApplicationState.ATTACHED);
    }

}
