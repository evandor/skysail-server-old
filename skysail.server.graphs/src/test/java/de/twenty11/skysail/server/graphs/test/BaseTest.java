package de.twenty11.skysail.server.graphs.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.restlet.security.MapVerifier;

import de.twenty11.skysail.common.app.ApplicationDescription;
import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.ext.osgimonitor.internal.OsgiMonitorComponent;
import de.twenty11.skysail.server.ext.osgimonitor.internal.OsgiMonitorUrlMapper;
import de.twenty11.skysail.server.ext.osgimonitor.internal.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.graphs.internal.GraphsSkysailApplication;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

public class BaseTest {

    protected GraphsSkysailApplication graphApplication;
    protected OsgiMonitorViewerApplication osgiMonitorViewerApplication;
    protected Restlet inboundRoot;
    protected ObjectMapper mapper = new ObjectMapper();

    protected BundleContext setupBundleContextMock() throws InvalidSyntaxException {
        BundleContext contextMock = mock(BundleContext.class);

        ServiceReference[] appDescriptorServiceReferencesMock = setupAppDescriptorServiceReferencesMock();
        ServiceReference[] appUrlMapperServiceReferencesMock = setupUrlMapperServiceReferencesMock();

        when(contextMock.getAllServiceReferences(ApplicationDescriptor.class.getName(), null)).thenReturn(
                appDescriptorServiceReferencesMock);
        when(contextMock.getAllServiceReferences(UrlMapper.class.getName(), null)).thenReturn(
                appUrlMapperServiceReferencesMock);
        when(contextMock.getService(appDescriptorServiceReferencesMock[0])).thenReturn(new ApplicationDescriptor() {

            @Override
            public ApplicationDescription getApplicationDescription() {
                return new ApplicationDescription("appName", "desc", "path");
            }
        });
        when(contextMock.getService(appUrlMapperServiceReferencesMock[0])).thenReturn(new UrlMapper() {
            @Override
            public Map<String, String> provideUrlMapping() {
                Map<String,String> result = new HashMap<String,String>();
                result.put("/testpath1", GraphTestClass.class.getName());
                result.put("/testpath2/{name}", GraphTestClass.class.getName());
                return result;
            }
        });
        return contextMock;
    }

    private ServiceReference[] setupAppDescriptorServiceReferencesMock() {
        ServiceReference[] serviceReferences = new ServiceReference[1];
        ServiceReference serviceReference = mock(ServiceReference.class);
        Bundle bundleMock = setupBundleMock();
        when(serviceReference.getBundle()).thenReturn(bundleMock);
        serviceReferences[0] = serviceReference;
        return serviceReferences;
    }

    private ServiceReference[] setupUrlMapperServiceReferencesMock() {
        ServiceReference[] serviceReferences = new ServiceReference[1];
        ServiceReference serviceReference = mock(ServiceReference.class);
        Bundle bundleMock = setupUrlMapperBundleMock();
        when(serviceReference.getBundle()).thenReturn(bundleMock);
        serviceReferences[0] = serviceReference;
        return serviceReferences;
    }

    private Bundle setupBundleMock() {
        Bundle bundleWithGraphAnnotation = mock(Bundle.class);
        Enumeration<URL> classes = new Enumeration<URL>() {

            boolean moreElements = true;

            @Override
            public boolean hasMoreElements() {
                return moreElements;
            }

            @Override
            public URL nextElement() {
                moreElements = false;
                return this.getClass().getResource("GraphTestClass.class");
            }
        };
        when(bundleWithGraphAnnotation.findEntries("/", "*.class", true)).thenReturn(classes);
        return bundleWithGraphAnnotation;
    }

    private Bundle setupUrlMapperBundleMock() {
        Bundle bundle = mock(Bundle.class);
        //when(bundle.).thenReturn(classes);
        return bundle;
    }

    protected OsgiMonitorViewerApplication setUpOsgiMonitorApplication() throws ClassNotFoundException {
        MapVerifier verifier = new MapVerifier();
        verifier.getLocalSecrets().put("admintest", "testpassword".toCharArray());
        ComponentContext contextMock = mock(ComponentContext.class);
        OsgiMonitorComponent component = new OsgiMonitorComponent(contextMock, verifier);
        osgiMonitorViewerApplication = component.getApplication();
        Application.setCurrent(osgiMonitorViewerApplication);
        inboundRoot = osgiMonitorViewerApplication.getInboundRoot();
        addMappings(new OsgiMonitorUrlMapper());
        return osgiMonitorViewerApplication;
    }

    protected void addMappings(UrlMapper urlMapper) throws ClassNotFoundException {
        Map<String, String> urlMapping = urlMapper.provideUrlMapping();
        for (Map.Entry<String, String> mapping : urlMapping.entrySet()) {
            @SuppressWarnings("unchecked")
            Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class.forName(mapping
                    .getValue());
            osgiMonitorViewerApplication.attachToRouter("" + mapping.getKey(), resourceClass);
        }
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
        ChallengeResponse authentication = new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "admintest",
                "testpassword");
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
