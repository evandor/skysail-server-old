package de.twentyeleven.skysail.server.restlet.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.security.Role;

import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twentyeleven.skysail.server.restlet.ApplicationsResource;
import de.twentyeleven.skysail.server.restlet.internal.MyApplication;



public class ApplicationsResourceTest extends BaseTests {
    
    private ApplicationsResource resource;

	@Before
    public void setUp() throws Exception {
        MyApplication spy = setUpRestletApplication();
        BundleContext context = mock(BundleContext.class);
        
        Application application = mock(Application.class);
        when(application.getAuthor()).thenReturn("theAuthor");
        when(application.getName()).thenReturn("theName");
        when(application.getDescription()).thenReturn("theDescription");
        List<Role> theRoles = new ArrayList<Role>();
        Role role = new Role();
        theRoles.add(role);
		when(application.getRoles()).thenReturn(theRoles);
		//when(context.getBundles()).thenReturn(bundles);
        ServiceReference applicationProviderService = mock(ServiceReference.class);
        
		ServiceReference[] serviceReferences = new ServiceReference[] {applicationProviderService};
		when(context.getAllServiceReferences(ApplicationProvider.class.getName(), null)).thenReturn(serviceReferences);
		ApplicationProvider applicationProvider = mock(ApplicationProvider.class);
		when(applicationProvider.getApplication()).thenReturn(application);
		when(context.getService(serviceReferences[0])).thenReturn(applicationProvider);
		when(spy.getBundleContext()).thenReturn(context);

        resource = new ApplicationsResource();
        Request request = mock(Request.class);
        ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        attributes.putIfAbsent("bundleId", "99");
        when(request.getAttributes()).thenReturn(attributes);
        resource.init(spy.getContext(), request, null);
    }

    // @Test
    // public void returns_applications_with_proper_values() throws Exception {
    // List<ApplicationDescriptor> applications = getApplications();
    // assertThat(applications.size(), is(equalTo(1)));
    // assertThat(applications.get(0).getAuthor(), is(equalTo("theAuthor")));
    // assertThat(applications.get(0).getName(), is(equalTo("theName")));
    // assertThat(applications.get(0).getDescription(), is(equalTo("theDescription")));
    // assertThat(applications.get(0).getRoles().size(), is(equalTo(1)));
    //
    // }
//
//    @Test
//    @Ignore
//    public void gives_error_message_for_post_when_location_doesnt_start_with_prefix() throws Exception {
//    	Representation answer = resource.install("wrongLocation");
//    	assertThat(answer.getText(), is(equalTo("location didn't start with 'prefix'")));
//    }


}
