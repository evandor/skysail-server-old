package de.twenty11.skysail.server.graphs.internal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.restlet.Application;

import de.twenty11.skysail.server.graphs.test.BaseTest;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class GraphsModelTest2 extends BaseTest {

    @Before
    public void testUp() throws Exception {
        setUpOsgiMonitorApplication();
    }

    @Test
    public void creates_the_proper_graphs_urlMapping() throws Exception {
        
        GraphsComponent graphComponent = new GraphsComponent();
        graphApplication = graphComponent.getApplication();
        ContextGraphModelProvider provider = new ContextGraphModelProvider(setupBundleContextMock());
        GraphsSkysailApplication.setGraphModelProvider(provider);
        Application.setCurrent(graphApplication);
        inboundRoot = graphApplication.getInboundRoot();
        Map<String, String> urlMapping = new GraphsUrlMapper().provideUrlMapping();
        assertThat(urlMapping.size(), is(equalTo(2)));
    }
    
   
}
