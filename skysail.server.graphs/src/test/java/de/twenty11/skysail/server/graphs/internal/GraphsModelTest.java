package de.twenty11.skysail.server.graphs.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;

import javassist.bytecode.ClassFile;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.graphs.GraphDetails;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GraphsModelTest {

	private GraphsModel graphsModel;
	private ClassFile cf;
	@Before
	public void testUp() throws Exception {
		graphsModel = new GraphsModel();
		InputStream resourceAsStream = this.getClass().getResourceAsStream("GraphTestClass.class");
        DataInputStream dstream = new DataInputStream(new BufferedInputStream(resourceAsStream));
        cf = new ClassFile(dstream);
	}

	@Test
	public void test() throws Exception {
		//graphsModel.examineClassFile(cf);
//		List<GraphDetails> allGraphs = graphsModel.getAllGraphs();
//		assertThat(allGraphs.size(), is(equalTo(1)));
	}
}
