package de.twentyeleven.skysail.server.restlet.test;



import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.codehaus.jackson.map.ObjectMapper;
import org.mockito.Mockito;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.security.MapVerifier;

import de.twenty11.skysail.common.responses.Response;
import de.twentyeleven.skysail.server.restlet.internal.MyApplication;

public class BaseTests {

    protected MyApplication osgiMonitorViewerApplication;
    protected Restlet inboundRoot;
    protected ObjectMapper mapper = new ObjectMapper();

    //private ComponentContext componentContextMock = mock(ComponentContext.class);
    
    protected MyApplication setUpRestletApplication()
            throws ClassNotFoundException {
    	
        MapVerifier secretVerifier = new MapVerifier();
        secretVerifier.getLocalSecrets().put("testadmin", "testpassword".toCharArray());
        // OsgiMonitorComponent osgiMonitorComponent = new OsgiMonitorComponent(componentContextMock, secretVerifier);
        osgiMonitorViewerApplication = new MyApplication();
        osgiMonitorViewerApplication.setVerifier(secretVerifier);

        MyApplication spy = Mockito.spy(osgiMonitorViewerApplication);
        Application.setCurrent(spy);
        inboundRoot = osgiMonitorViewerApplication.getInboundRoot();
        //addMappings();
        return spy;
    }


//    private void addMappings() throws ClassNotFoundException {
//        Map<String, String> urlMapping = new OsgiMonitorUrlMapper().provideUrlMapping();
//        for (Map.Entry<String, String> mapping : urlMapping.entrySet()) {
//            @SuppressWarnings("unchecked")
//            Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class.forName(mapping
//                    .getValue());
//            osgiMonitorViewerApplication.attachToRouter("" + mapping.getKey(), resourceClass);
//        }
//    }

//    protected List<BundleDescriptor> getBundles() throws Exception {
//        org.restlet.Response response = get("bundles");
//        assertDefaults(response);
//        Representation entity = response.getEntity();
//        Response<List<BundleDescriptor>> skysailResponse = mapper.readValue(entity.getText(),
//                new TypeReference<Response<List<BundleDescriptor>>>() {
//                });
//        assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
//        return skysailResponse.getData();
//    }

   

    protected org.restlet.Response get(String uri) {
        Request request = new Request(Method.GET, "/" + uri);
        return handleRequest(request);
    }

    protected org.restlet.Response handleRequest(Request request) {
        ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "testadmin",
                "testpassword");
        request.setChallengeResponse(authentication);
        org.restlet.Response response = new org.restlet.Response(request);
        inboundRoot.handle(request, response);
        return response;
    }

    protected void assertDefaults(org.restlet.Response response) {
        assertEquals(200, response.getStatus().getCode());
        assertThat(response.isEntityAvailable(), is(true));
        //assertThat(response.getEntity().getMediaType(), is(MediaType.APPLICATION_JSON));
    }

}
