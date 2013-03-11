package de.twentyeleven.skysail.server.restlet.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.security.MapVerifier;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.restlet.ApplicationDescriptor;
import de.twentyeleven.skysail.server.restlet.internal.MyApplication;

public class BaseTests {

	protected MyApplication myApplication;
	protected Restlet inboundRoot;
	protected ObjectMapper mapper = new ObjectMapper();

	// private ComponentContext componentContextMock =
	// mock(ComponentContext.class);

	protected MyApplication setUpRestletApplication()
			throws ClassNotFoundException {

		MapVerifier secretVerifier = new MapVerifier();
		secretVerifier.getLocalSecrets().put("testadmin",
				"testpassword".toCharArray());
        BundleContext bundleContext = Mockito.mock(BundleContext.class);
        myApplication = new MyApplication(bundleContext);
		myApplication.setVerifier(secretVerifier);

		MyApplication spy = Mockito.spy(myApplication);
		Application.setCurrent(spy);
		inboundRoot = myApplication.getInboundRoot();
		return spy;
	}

	protected List<ApplicationDescriptor> getApplications() throws Exception {
		org.restlet.Response response = get("applications");
		assertDefaults(response);
		Representation entity = response.getEntity();
		SkysailResponse<List<ApplicationDescriptor>> skysailResponse = mapper.readValue(
				entity.getText(),
				new TypeReference<SkysailResponse<List<ApplicationDescriptor>>>() {
				});
		assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(),
				is(true));
		return skysailResponse.getData();
	}

	protected org.restlet.Response get(String uri) {
		Request request = new Request(Method.GET, "/" + uri);
		return handleRequest(request);
	}

	protected org.restlet.Response handleRequest(Request request) {
		ChallengeResponse authentication = new ChallengeResponse(
				ChallengeScheme.HTTP_BASIC, "testadmin", "testpassword");
		request.setChallengeResponse(authentication);
		org.restlet.Response response = new org.restlet.Response(request);
		inboundRoot.handle(request, response);
		return response;
	}

	protected void assertDefaults(org.restlet.Response response) {
		assertEquals(200, response.getStatus().getCode());
		assertThat(response.isEntityAvailable(), is(true));
		// assertThat(response.getEntity().getMediaType(),
		// is(MediaType.APPLICATION_JSON));
	}

}
