package de.twenty11.skysail.server.internal;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.restlet.Application;
import org.restlet.security.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.osgi.internal.ApplicationLifecycle;
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.core.osgi.internal.Trigger;
import de.twenty11.skysail.server.restlet.SkysailApplication;

public class ApplicationsHolder {

    private static Logger logger = LoggerFactory.getLogger(ApplicationsHolder.class);

    private Map<Application, ApplicationLifecycle> lifecycles = new HashMap<Application, ApplicationLifecycle>();

    private OperatingSystemMXBean operatingSystemMxBean = ManagementFactory.getOperatingSystemMXBean();

    public void add(Application application) throws Exception {
        lifecycles.put(application, new ApplicationLifecycle());
    }

    public void attach(Application application, SkysailComponent restletComponent, Verifier verifier) throws Exception {

        logger.info("");
        logger.info("==================================================");
        logger.info(" >>> attaching application '{}' <<<", application.getName());

        // TODO set verifier the same way?
        if (application.getContext() != null) {
            application.getContext().getAttributes()
                    .put(Configuration.CONTEXT_OPERATING_SYSTEM_BEAN, operatingSystemMxBean);
        }
        if (application instanceof SkysailApplication) {
            ((SkysailApplication) application).setVerifier(verifier);
            logger.info(" >>> setting verifier from serverConfiguration");
            // ((SkysailApplication) application).setComponentContext(componentContext);

        }
        logger.info(" >>> attaching '{}' to defaultHost", "/" + application.getName());
        restletComponent.getDefaultHost().attach("/" + application.getName(), application);
        logger.info("==================================================");

        // TODO: move about into some trigger-action?
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
