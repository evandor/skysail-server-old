package de.twenty11.skysail.server.internal;

import org.osgi.service.component.ComponentContext;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.security.SecretVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailComponent extends Component {

    private static Logger logger = LoggerFactory.getLogger(SkysailComponent.class);

    public SkysailComponent(ComponentContext componentContext, SecretVerifier verifier) {

        getClients().add(Protocol.CLAP);
        getClients().add(Protocol.HTTP);
        getClients().add(Protocol.FILE);
        // getClients().add(Protocol.WAR);

        // Create a restlet application
        logger.info("new restlet application: {}", SkysailComponent.class.getName());
        // application = new OsgiMonitorViewerApplication("/static", componentContext.getBundleContext());
        // application.setVerifier(verifier);

        // Attach the application to the component and start it
        logger.info("attaching application and starting {}", this.toString());
        // getDefaultHost().attach(application);

        // LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/static/");
        //
        // CompositeClassLoader customCL = new CompositeClassLoader();
        // customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        // customCL.addClassLoader(Router.class.getClassLoader());
        // customCL.addClassLoader(this.getClass().getClassLoader());
        //
        // ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);
        //
        // getDefaultHost().attach("/" + OsgiMonitorApplicationDescriptor.APPLICATION_NAME + "/static",
        // staticDirectory);

    }
}
