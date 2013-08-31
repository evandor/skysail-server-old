package de.twenty11.skysail.server.core.restlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
import de.twenty11.skysail.server.core.restlet.testresources.MyEntityResource;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;

public class RequestHandlerTest {

    @Before
    public void setUp() {
    }

    @Test
    public void listOfEntities_is_retrieved_via_requestHandlerChain() {
        SkysailResponse<List<SimpleEntity>> entities = new MyListResource().getEntities();
        assertThat(entities.getData().size(), is(2));
    }

    @Test
    public void entity_is_retrieved_via_requestHandlerChain() {
        SkysailResponse<SimpleEntity> entities = new MyEntityResource(new RequestHandler<SimpleEntity>()).getEntity();
        assertThat(entities.getData().getName(), is("simple"));
    }

}
