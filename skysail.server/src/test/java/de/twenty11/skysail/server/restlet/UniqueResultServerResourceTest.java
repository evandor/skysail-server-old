package de.twenty11.skysail.server.restlet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.twenty11.skysail.common.responses.Response;

public class UniqueResultServerResourceTest {

	public class Dummy {

    }

	private UniqueResultServerResource<Dummy> uniqueResultServerResource;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		uniqueResultServerResource = new UniqueResultServerResource<Dummy>();
		uniqueResultServerResource.setApplication(new SkysailApplication("string", "string") {
			@Override
			protected void attach() {
			}
		});
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		Dummy data = null;
		Response entity = uniqueResultServerResource.getEntity(data);
	}

}
