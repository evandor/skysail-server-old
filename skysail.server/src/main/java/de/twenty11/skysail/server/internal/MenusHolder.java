package de.twenty11.skysail.server.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.core.osgi.internal.MenuLifecycle;
import de.twenty11.skysail.server.core.osgi.internal.MenuState;
import de.twenty11.skysail.server.core.osgi.internal.Trigger;

public class MenusHolder {

    private static Logger logger = LoggerFactory.getLogger(MenusHolder.class);

    private Map<Object, MenuLifecycle> lifecycles = new HashMap<Object, MenuLifecycle>();

    public void add(Object obj) throws Exception {
        lifecycles.put(obj, new MenuLifecycle());
    }

//    public void attach(Application application, SkysailComponent restletComponent, ServerConfiguration serverConfig, ConfigurationAdmin configAdmin) throws Exception {
  	public void attach(Object menu) throws Exception {

        logger.info("");
        logger.info("==================================================");
        //logger.info(" >>> attaching application '{}' <<<", application.getName());

//        // TODO set verifier the same way?
//        if (application.getContext() != null) {
//            application.getContext().getAttributes()
//                    .put(Configuration.CONTEXT_OPERATING_SYSTEM_BEAN, operatingSystemMxBean);
//        }
//        if (application instanceof SkysailApplication) {
//            ((SkysailApplication) application).setVerifier(serverConfig.getVerifier(configAdmin));
//            logger.info(" >>> setting verifier from serverConfiguration");
//
//            ((SkysailApplication) application).setServerConfiguration(serverConfig);
//            logger.info(" >>> setting ServerConfiguration");
//        }
//        logger.info(" >>> attaching '{}' to defaultHost", "/" + application.getName());
//        restletComponent.getDefaultHost().attach("/" + application.getName(), application);
//        logger.info("==================================================");

        lifecycles.get(menu).Fire(Trigger.ATTACH);
    }

    public List<Object> getMenusInState(MenuState state) {
        List<Object> result = new ArrayList<Object>();
        for (Entry<Object, MenuLifecycle> entry : lifecycles.entrySet()) {
            if (entry.getValue().getState().equals(state)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

}
