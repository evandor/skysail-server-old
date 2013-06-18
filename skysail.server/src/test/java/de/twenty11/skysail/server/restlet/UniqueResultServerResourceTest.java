package de.twenty11.skysail.server.restlet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Context;

import de.twenty11.skysail.common.responses.SkysailResponse;

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
        Context context = Mockito.mock(Context.class);
        uniqueResultServerResource.setApplication(new SkysailApplication() {
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
        SkysailResponse entity = uniqueResultServerResource.getEntity(data);
    }

}
