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

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.restlet.DefaultResource.AvailableApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;

public class DefaultResource extends ListServerResource<AvailableApplication> {

    public class AvailableApplication implements Presentable {

        private String name;

        public AvailableApplication(Application application) {
            this.name = application.getName();
        }

        @Override
        public String getHeader() {
            return name;
        }

        @Override
        public String getHeaderLink() {
            return name;
        }

        @Override
        public Map<String, Object> getContent() {
            return Collections.emptyMap();
        }

        @Override
        public String getImageIdentifier() {
            return "icon-cog";
        }

    }

    private List<Application> applications = new ArrayList<Application>();

    public DefaultResource() {
        setName("Skysail Server Available Applications Resource");
        setDescription("The resource containing the list of available applications");
    }

    @Get("html|json")
    public Response<List<AvailableApplication>> getApplications() {
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
                for (ServiceReference serviceReference : allServiceReferences) {
                    ApplicationProvider provider = (ApplicationProvider) bundleContext.getService(serviceReference);
                    applications.add(provider.getApplication());
                }
            } catch (InvalidSyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
