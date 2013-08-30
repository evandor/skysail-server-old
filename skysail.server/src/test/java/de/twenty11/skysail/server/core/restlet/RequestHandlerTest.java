package de.twenty11.skysail.server.core.restlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.testentities.Entity;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;



public class RequestHandlerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void listOfEntities_is_retrieved_via_requestHandlerChain() {
        SkysailResponse<List<Entity>> entities = new MyListResource(new RequestHandler<Entity>()).getEntities();
        assertThat(entities.getData().size(), is(2));
    }

}
