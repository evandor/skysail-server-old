package de.twenty11.skysail.server.core.osgi.internal;

import com.googlecode.stateless4j.StateMachine;

public class ApplicationLifecycle extends StateMachine<ApplicationState, Trigger> {

    public ApplicationLifecycle() throws Exception {
        super(ApplicationState.NEW);

        Configure(ApplicationState.NEW).Permit(Trigger.ATTACH, ApplicationState.ATTACHED);
    }

}
