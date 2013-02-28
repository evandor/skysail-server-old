package de.twentyeleven.skysail.server.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.restlet.Application;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.restlet.ApplicationDescriptor;
import de.twenty11.skysail.common.restlet.RestfulApplications;
import de.twenty11.skysail.server.restlet.ListServerResource;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twentyeleven.skysail.server.restlet.internal.MyApplication;

public class ApplicationsResource extends ListServerResource<ApplicationDescriptor> implements RestfulApplications {

	private List<Application> applications = new ArrayList<Application>();
	
	public ApplicationsResource() {
		setName("restlet applications resource");
		setDescription("The resource containing the list of restlet applications");
	}
	
	@Override
	@Get("html|json")
	public Response<List<ApplicationDescriptor>> getApplications() {
		return getEntities(allApplications(), "all Applications");
	}
	
	private List<ApplicationDescriptor> allApplications() {
		List<ApplicationDescriptor> result = new ArrayList<ApplicationDescriptor>();
		for (Application application : applications) {
			ApplicationDescriptor descriptor = new ApplicationDescriptor(application);
			result.add(descriptor);
		}
		return result;
	}

		@Override
	protected void doInit() throws ResourceException {
		MyApplication app = (MyApplication) getApplication();
		BundleContext bundleContext = app.getBundleContext();
		if (bundleContext == null) {
			applications = Collections.emptyList();
		} else {
			ServiceReference[] allServiceReferences;
			try {
				allServiceReferences = bundleContext.getAllServiceReferences(ApplicationProvider.class.getName(), null);
				for (ServiceReference serviceReference : allServiceReferences) {
					ApplicationProvider provider = (ApplicationProvider)bundleContext.getService(serviceReference);
					applications.add(provider.getApplication());
				}
			} catch (InvalidSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
   
}
