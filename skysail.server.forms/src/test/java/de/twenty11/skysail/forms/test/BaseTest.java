package de.twenty11.skysail.forms.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.mockito.Mockito;
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
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.forms.internal.FormModelProvider;
import de.twenty11.skysail.server.forms.internal.FormsComponent;
import de.twenty11.skysail.server.forms.internal.FormsModel;
import de.twenty11.skysail.server.forms.internal.FormsUrlMapper;
import de.twenty11.skysail.server.forms.internal.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class BaseTest {

    protected SkysailApplication skysailApplication;
    protected Restlet inboundRoot;
    protected ObjectMapper mapper = new ObjectMapper();

    protected void addMappings() throws ClassNotFoundException {
        Map<String, String> urlMapping = new FormsUrlMapper().provideUrlMapping();
        for (Map.Entry<String, String> mapping : urlMapping.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class.forName(mapping
                    .getValue());
            skysailApplication.attachToRouter("" + mapping.getKey(), resourceClass);
        }
    }

    protected SkysailApplication setUpRestletApplication() throws ClassNotFoundException {
        FormsComponent dbViewerComponent = new FormsComponent();
        skysailApplication = dbViewerComponent.getApplication();

        //final ApplicationDescription spy = Mockito.spy(skysailApplication);
        skysailApplication.setFormModelProvider(new FormModelProvider() {
            
            @Override
            public Map<ApplicationDescriptor, FormsModel> getFormModels() {
                Map<ApplicationDescriptor, FormsModel> result = new HashMap<ApplicationDescriptor, FormsModel>();
                ApplicationDescriptor appService = new ApplicationDescriptor() {
                    
                    @Override
                    public de.twenty11.skysail.common.app.ApplicationDescription getApplicationDescription() {
                        return new de.twenty11.skysail.common.app.ApplicationDescription("testapp","","");
                    }
                };
                FormsModel formsModel = new FormsModel();
                result.put(appService, formsModel);
                return result;
            }
        });
        Application.setCurrent(skysailApplication);
        inboundRoot = skysailApplication.getInboundRoot();
        addMappings();
        return skysailApplication;
    }

    protected List<FormDetails> getForms() throws Exception {
        org.restlet.Response response = get("/testapp/forms");
        assertDefaults(response);
        Representation entity = response.getEntity();
        Response<List<FormDetails>> skysailResponse = mapper.readValue(entity.getText(),
                new TypeReference<Response<List<FormDetails>>>() {
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

    // protected ConnectionDetails getConnection(String connectionName) throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName);
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<ConnectionDetails> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<ConnectionDetails>>() {
    // });
    // ConnectionDetails data = skysailResponse.getData();
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return data;
    // }
    //
    // protected List<SchemaDetails> getSchemas(String connectionName) throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName + "/schemas");
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<List<SchemaDetails>> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<List<SchemaDetails>>>() {
    // });
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return skysailResponse.getData();
    // }
    //
    // protected List<String> getTables(String connectionName, String schemaName) throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName + "/schemas/" + schemaName
    // + "/tables");
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<List<String>> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<List<String>>>() {
    // });
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return skysailResponse.getData();
    // }
    //
    // protected List<ColumnsDetails> getColumns(String connectionName, String schemaName, String tableName)
    // throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName + "/schemas/" + schemaName
    // + "/tables/" + tableName + "/columns");
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<List<ColumnsDetails>> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<List<ColumnsDetails>>>() {
    // });
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return skysailResponse.getData();
    // }
    //
    // protected List<ConstraintDetails> getConstraints(String connectionName, String schemaName, String tableName)
    // throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName + "/schemas/" + schemaName
    // + "/tables/" + tableName + "/constraints");
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<List<ConstraintDetails>> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<List<ConstraintDetails>>>() {
    // });
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return skysailResponse.getData();
    // }
    //
    // protected GridData getData(String connectionName, String schemaName, String tableName)
    // throws Exception {
    // org.restlet.Response response = get("/dbviewer/connections/" + connectionName + "/schemas/" + schemaName
    // + "/tables/" + tableName + "/data");
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<GridData> skysailResponse = mapper.readValue(entity.getText(),
    // new TypeReference<Response<GridData>>() {
    // });
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // return skysailResponse.getData();
    // }
    //
    // protected void deleteConnection(String connectionName) throws Exception {
    // org.restlet.Response response = delete("/dbviewer/connections/" + connectionName);
    // assertDefaults(response);
    // Representation entity = response.getEntity();
    // Response<String> skysailResponse = mapper.readValue(entity.getText(), new TypeReference<Response<String>>() {
    // });
    // String data = skysailResponse.getData();
    // assertThat(skysailResponse.getMessage(), skysailResponse.getSuccess(), is(true));
    // assertThat(skysailResponse.getMessage(), skysailResponse.getMessage(),
    // is("deleted entity 'name, username, url'"));
    // }
    //
    // private org.restlet.Response delete(String uri) {
    // Request request = new Request(Method.DELETE, uri);
    // return handleRequest(request);
    // }
    //
    //
    // protected org.restlet.Response post(String uri, Object connection) throws JsonGenerationException,
    // JsonMappingException, IOException {
    // Request request = new Request(Method.POST, uri);
    // String writeValueAsString = mapper.writeValueAsString(connection);
    // request.setEntity(writeValueAsString, MediaType.APPLICATION_JSON);
    // return handleRequest(request);
    // }
    //
    //
    //
    // protected void setUpPersistence(ApplicationDescription spy) {
    // final EntityManagerFactory emf = Persistence.createEntityManagerFactory("testPU");
    // Mockito.doAnswer(new Answer<EntityManager>() {
    // @Override
    // public EntityManager answer(InvocationOnMock invocation) throws Throwable {
    // return emf.createEntityManager();
    // }
    //
    // }).when(spy).getEntityManager();
    // }
    //

}
