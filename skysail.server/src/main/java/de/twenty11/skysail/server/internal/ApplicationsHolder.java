package de.twenty11.skysail.server.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.restlet.Application;

import de.twenty11.skysail.server.core.osgi.internal.ApplicationLifecycle;
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.core.osgi.internal.Trigger;

public class ApplicationsHolder {

    private Map<Application, ApplicationLifecycle> lifecycles = new HashMap<Application, ApplicationLifecycle>();

    public void add(Application application) throws Exception {
        lifecycles.put(application, new ApplicationLifecycle());
    }

    public void attach(Application application, SkysailComponent restletComponent) throws Exception {
        restletComponent.getDefaultHost().attach("/" + application.getName(), application);
        lifecycles.get(application).Fire(Trigger.ATTACH);
    }

    public List<Application> getApplicationsInState(ApplicationState state) {
        List<Application> result = new ArrayList<Application>();
        for (Entry<Application, ApplicationLifecycle> entry : lifecycles.entrySet()) {
            if (entry.getValue().getState().equals(state)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
