//package de.twenty11.skysail.server.webapp.internal;
//
//import org.osgi.framework.BundleActivator;
//import org.osgi.framework.BundleContext;
//import org.restlet.Application;
//
//import de.twenty11.skysail.server.services.ApplicationProvider;
//
//public class Activator implements BundleActivator, ApplicationProvider {
//
//    private WebappApplication webappApplication;
//
//    @Override
//    public void start(BundleContext context) throws Exception {
//        // webappComponent = new WebappComponent();
//        webappApplication = new WebappApplication();
//    }
//
//    @Override
//    public void stop(BundleContext context) throws Exception {
//        webappApplication = null;
//    }
//
//    @Override
//    public Application getApplication() {
//        return webappApplication;
//    }
//
// }
