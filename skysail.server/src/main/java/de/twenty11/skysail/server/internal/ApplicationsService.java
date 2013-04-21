package de.twenty11.skysail.server.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.restlet.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.services.ApplicationProvider;

public class ApplicationsService {

    private static Logger logger = LoggerFactory.getLogger(ApplicationsService.class);

    public static List<Application> getApplications(BundleContext bundleContext) {
        if (bundleContext == null) {
            return Collections.emptyList();
        } else {
            ServiceReference[] allServiceReferences;
            List<Application> applications = new ArrayList<Application>();
            try {
                allServiceReferences = bundleContext.getAllServiceReferences(ApplicationProvider.class.getName(), null);
                if (allServiceReferences != null) {
                    for (ServiceReference serviceReference : allServiceReferences) {
                        ApplicationProvider provider = (ApplicationProvider) bundleContext.getService(serviceReference);
                        if (provider == null) {
                            logger.warn("ApplicationProvider from ServiceRegistry was null!");
                            continue;
                        }
                        applications.add(provider.getApplication());
                    }
                }
            } catch (InvalidSyntaxException e) {
                e.printStackTrace();
            }
            return applications;
        }
    }

}
