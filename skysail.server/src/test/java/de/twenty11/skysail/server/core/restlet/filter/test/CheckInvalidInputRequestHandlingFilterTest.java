package de.twenty11.skysail.server.core.restlet.filter.test;

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
import org.restlet.data.Reference;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource;
import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputFilter;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;

public class CheckInvalidInputRequestHandlingFilterTest {

    private CheckInvalidInputFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>> filterUnderTest;
    private Request request;
    private Reference ref;
    private Form myForm;
    private ListServerResource<SimpleEntity> resource;

    @Before
    public void setUp() throws Exception {
        filterUnderTest = new CheckInvalidInputFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>>();
        request = Mockito.mock(Request.class);
        ref = Mockito.mock(Reference.class);
        myForm = new Form();

        resource = new MyListResource();

        ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        attributes.put(UniqueResultServerResource.SKYSAIL_SERVER_RESTLET_FORM, myForm);
        Mockito.when(request.getAttributes()).thenReturn(attributes);
    }

    @Test
    public void should_let_string_without_tags_pass() {
        myForm.add("name", "myname");

        SkysailResponse response = filterUnderTest.handle(resource, request).getSkysailResponse();

        assertThat(response.getSuccess(), is(true));
    }

    @Test
    public void should_not_let_string_with_tags_pass() {
        myForm.add("name", "myname<script></script>");

        SkysailResponse<?> response = filterUnderTest.handle(resource, request).getSkysailResponse();

        assertThat(response.getSuccess(), is(false));
    }
}
