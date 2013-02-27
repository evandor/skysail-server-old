//package de.twentyeleven.skysail.server.restlet.internal;
//
//import org.osgi.service.component.ComponentContext;
//import org.restlet.Component;
//import org.restlet.data.Protocol;
//import org.restlet.security.SecretVerifier;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * Concurrency note from parent class: instances of this class or its subclasses can be invoked by several threads at
// * the same time and therefore must be thread-safe. You should be especially careful when storing state in member
// * variables.
// * 
// * @author carsten
// * 
// */
//public class MyComponent extends Component {
//
//    /** slf4j based log4jLogger implementation. */
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    private MyApplication application;
//
//    /**
//     * @param componentContext
//     * 
//     */
//    public MyComponent(ComponentContext componentContext, SecretVerifier verifier) {
//
//        getClients().add(Protocol.CLAP);
//        getClients().add(Protocol.HTTP);
//        getClients().add(Protocol.FILE);
//        // getClients().add(Protocol.WAR);
//
//        // Create a restlet application
//        logger.info("new restlet application: {}", MyApplication.class.getName());
//        application = new MyApplication("/static", componentContext.getBundleContext());
//        application.setVerifier(verifier);
//
//        // Attach the application to the component and start it
//        logger.info("attaching application and starting {}", this.toString());
//        getDefaultHost().attach(application);
//
//    }
//
//    @Override
//    public MyApplication getApplication() {
//        return this.application;
//    }
//}
