package de.twenty11.skysail.server.core.restlet.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.RequestHandler;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
import de.twenty11.skysail.server.core.restlet.testresources.MyEntityResource;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;
import org.restlet.data.Reference;

public class RequestHandlerTest {

    Request mockedRequest;

    Form form;

    ConcurrentMap<String,Object> attributes;

    @Before
    public void setUp() {
        attributes = new ConcurrentHashMap<String, Object>();
        form = new Form();

        mockedRequest = Mockito.mock(Request.class);
        Reference resourceRef = Mockito.mock(Reference.class);
        Mockito.when(mockedRequest.getResourceRef()).thenReturn(resourceRef);
        Mockito.when(mockedRequest.getAttributes()).thenReturn(attributes);
    }

    /* GET */

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

    /* POST */

    @Test
    public void posting_entity_via_requestHandlerChain_is_successful() {
        form.add("name", "name");

        MyListResource myListResource = new MyListResource();
        myListResource.setRequest(mockedRequest);

        SkysailResponse<?> result = myListResource.addFromForm(form);
        assertThat(result.getSuccess(), is(true));
    }

}
