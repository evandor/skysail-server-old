package de.twenty11.skysail.server.internal;

import org.osgi.service.component.ComponentContext;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailComponent extends Component {

    private static Logger logger = LoggerFactory.getLogger(SkysailComponent.class);

    public SkysailComponent(ComponentContext componentContext) {

        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);
        getClients().add(Protocol.FILE);
        // getClients().add(Protocol.WAR);

        // Create a restlet component
        logger.info("new restlet component: {}", SkysailComponent.class.getName());
    }
}
