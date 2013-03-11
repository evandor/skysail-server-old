//package de.twenty11.skysail.server.management.internal;
//
//import org.osgi.framework.BundleContext;
//import org.osgi.service.component.ComponentContext;
//
//import de.twenty11.skysail.common.app.ApplicationDescription;
//import de.twenty11.skysail.server.services.ApplicationDescriptor;
//
//public class ManagementApplicationDescriptor implements ApplicationDescriptor {
//
//    public static final String APPLICATION_NAME = "regprox";
//    private static BundleContext bundleContext;
//
//    @Override
//    public ApplicationDescription getApplicationDescription() {
//        return new ApplicationDescription(APPLICATION_NAME, "regprox", APPLICATION_NAME);
//    }
//
//    protected void activate(final ComponentContext component) {
//        bundleContext = component.getBundleContext();
//    }
//
//
// }
