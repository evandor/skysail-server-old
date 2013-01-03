package de.twenty11.skysail.server.management.internal;

import org.osgi.framework.BundleContext;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concurrency note from parent class: instances of this class or its subclasses can be invoked by several threads at
 * the same time and therefore must be thread-safe. You should be especially careful when storing state in member
 * variables.
 * 
 * @author carsten
 * 
 */
public class ManagementComponent extends Component {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ManagementApplication application;

    /**
     * @param bundleContext
     * 
     */
    public ManagementComponent(BundleContext bundleContext) {
        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);

        // Create a restlet application
        logger.info("new restlet application: {}", ManagementApplication.class.getName());
        application = new ManagementApplication(bundleContext, "/static");

        // Attach the application to the component and start it
        logger.info("attaching application and starting {}", this.toString());
        getDefaultHost().attachDefault(application);
    }

    @Override
    public ManagementApplication getApplication() {
        return this.application;
    }
}
