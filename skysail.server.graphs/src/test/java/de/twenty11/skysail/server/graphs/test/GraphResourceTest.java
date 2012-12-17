package de.twenty11.skysail.server.graphs.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.server.graphs.internal.GraphsSkysailApplication;

public class GraphResourceTest extends BaseTest {
    @Before
    public void setUp() throws Exception {
        GraphsSkysailApplication spy = setUpRestletApplication();
        //setUpPersistence(spy);
    }
    
    @Test
    @Ignore
    public void test() throws Exception {
        List<GraphDetails> graphs = getGraphs();
        assertThat(graphs.size(), is(equalTo(0)));
    }

   
    
}
