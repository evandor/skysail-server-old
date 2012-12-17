package de.twenty11.skysail.server.graphs.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.graphs.internal.GraphModelProvider;
import de.twenty11.skysail.server.graphs.internal.GraphsComponent;
import de.twenty11.skysail.server.graphs.internal.GraphsModel;
import de.twenty11.skysail.server.graphs.internal.GraphsSkysailApplication;
import de.twenty11.skysail.server.graphs.internal.GraphsUrlMapper;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class BaseTest {

    protected GraphsSkysailApplication formsSkysailApplication;
    protected Restlet inboundRoot;
    protected ObjectMapper mapper = new ObjectMapper();

    protected void addMappings() throws ClassNotFoundException {
        Map<String, String> urlMapping = new GraphsUrlMapper().provideUrlMapping();
        for (Map.Entry<String, String> mapping : urlMapping.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class.forName(mapping
                    .getValue());
            formsSkysailApplication.attachToRouter("" + mapping.getKey(), resourceClass);
        }
    }

    protected GraphsSkysailApplication setUpRestletApplication() throws ClassNotFoundException {
        GraphsComponent dbViewerComponent = new GraphsComponent();
        formsSkysailApplication = dbViewerComponent.getApplication();

        //final ApplicationDescription spy = Mockito.spy(formsSkysailApplication);
        formsSkysailApplication.setFormModelProvider(new GraphModelProvider() {
            
            @Override
            public Map<ApplicationDescriptor, GraphsModel> getGraphModels() {
                Map<ApplicationDescriptor, GraphsModel> result = new HashMap<ApplicationDescriptor, GraphsModel>();
                ApplicationDescriptor appService = new ApplicationDescriptor() {
                    
                    @Override
                    public de.twenty11.skysail.common.app.ApplicationDescription getApplicationDescription() {
                        return new de.twenty11.skysail.common.app.ApplicationDescription("testapp","","");
                    }
                };
                GraphsModel formsModel = new GraphsModel();
                result.put(appService, formsModel);
                return result;
            }
        });
        Application.setCurrent(formsSkysailApplication);
        inboundRoot = formsSkysailApplication.getInboundRoot();
        addMappings();
        return formsSkysailApplication;
    }

    protected List<GraphDetails> getGraphs() throws Exception {
        org.restlet.Response response = get("/testapp/graphs");
        assertDefaults(response);
        Representation entity = response.getEntity();
        Response<List<GraphDetails>> skysailResponse = mapper.readValue(entity.getText(),
                new TypeReference<Response<List<GraphDetails>>>() {
                });
        assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
        return skysailResponse.getData();
    }

    protected org.restlet.Response get(String uri) {
        Request request = new Request(Method.GET, uri);
        return handleRequest(request);
    }

    protected org.restlet.Response handleRequest(Request request) {
        ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "scott", "tiger");
        request.setChallengeResponse(authentication);
        org.restlet.Response response = new org.restlet.Response(request);
        inboundRoot.handle(request, response);
        return response;
    }

    protected void assertDefaults(org.restlet.Response response) {
        assertEquals(200, response.getStatus().getCode());
        assertThat(response.isEntityAvailable(), is(true));
        assertThat(response.getEntity().getMediaType(), is(MediaType.APPLICATION_JSON));
    }

   
}
