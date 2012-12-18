package de.twenty11.skysail.server.graphs.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;

import javassist.bytecode.ClassFile;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;

import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.server.ext.osgimonitor.internal.OsgiMonitorViewerApplication;
import de.twenty11.skysail.server.graphs.test.BaseTest;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GraphsModelTest extends BaseTest {

	private GraphsModel graphsModel;
	private ClassFile cf;
	private GraphsSkysailApplication graphsSkysailApplication;
	private OsgiMonitorViewerApplication osgiMonitorApplication;
	@Before
	public void testUp() throws Exception {
		osgiMonitorApplication = setUpOsgiMonitorApplication();
		graphsSkysailApplication = setUpGraphsApplication();
		
		graphsModel = new GraphsModel();
		InputStream resourceAsStream = this.getClass().getResourceAsStream("GraphTestClass.class");
        DataInputStream dstream = new DataInputStream(new BufferedInputStream(resourceAsStream));
        cf = new ClassFile(dstream);
	}

	@Test
	public void test() throws Exception {
		 ChallengeResponse authentication = new ChallengeResponse(
	                ChallengeScheme.HTTP_BASIC, "scott", "tiger");
		graphsModel.examineClassFile(cf, authentication);
////		List<GraphDetails> allGraphs = graphsModel.getAllGraphs();
////		assertThat(allGraphs.size(), is(equalTo(1)));
	}
}
