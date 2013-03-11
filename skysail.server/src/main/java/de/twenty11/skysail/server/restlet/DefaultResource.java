package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.restlet.Application;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.PresentableHeader;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.restlet.DefaultResource.AvailableApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;

public class DefaultResource extends ListServerResource<AvailableApplication> {

    private static Logger logger = LoggerFactory.getLogger(DefaultResource.class);

    public class AvailableApplication implements Presentable {

        private String name;

        public AvailableApplication(Application application) {
            this.name = application.getName();
        }

        @Override
        public PresentableHeader getHeader() {
            return new PresentableHeader.Builder(name).setLink(name).setImage("icon-cog").build();
        }

        @Override
        public Map<String, Object> getContent() {
            return Collections.emptyMap();
        }
    }

    private List<Application> applications = new ArrayList<Application>();

    public DefaultResource() {
        setName("Skysail Server Available Applications Resource");
        setDescription("The resource containing the list of available applications");
    }

    @Get("html|json")
    public SkysailResponse<List<AvailableApplication>> getApplications() {
        return getEntities(allApplications(), "all Applications");
    }

    private List<AvailableApplication> allApplications() {
        List<AvailableApplication> result = new ArrayList<AvailableApplication>();
        for (Application application : applications) {
            if (application.getName().equals("static")) {
                continue;
            }
            AvailableApplication descriptor = new AvailableApplication(application);
            result.add(descriptor);
        }
        return result;
    }

    @Override
    protected void doInit() throws ResourceException {
        SkysailApplication app = (SkysailApplication) getApplication();
        BundleContext bundleContext = app.getBundleContext();
        if (bundleContext == null) {
            applications = Collections.emptyList();
        } else {
            ServiceReference[] allServiceReferences;
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
