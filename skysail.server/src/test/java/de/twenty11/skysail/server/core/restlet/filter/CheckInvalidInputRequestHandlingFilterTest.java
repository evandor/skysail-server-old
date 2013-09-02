package de.twenty11.skysail.server.core.restlet.filter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.SkysailRequestHandlingFilterTest.Entity;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;

public class CheckInvalidInputRequestHandlingFilterTest {

    private CheckInvalidInputRequestHandlingFilter<Entity> filterUnderTest;
    private Request request;
    private Reference ref;
    private Form myForm;
    private ListServerResource resource;

    @Before
    public void setUp() throws Exception {
        filterUnderTest = new CheckInvalidInputRequestHandlingFilter<Entity>();
        request = Mockito.mock(Request.class);
        ref = Mockito.mock(Reference.class);
        myForm = new Form();

        resource = new MyListResource(myForm);

        Mockito.when(ref.getQueryAsForm()).thenReturn(myForm);
        Mockito.when(request.getResourceRef()).thenReturn(ref);
    }

    @Test
    public void should_let_string_without_tags_pass() {
        myForm.add("name", "myname");

        SkysailResponse<?> response = filterUnderTest.handle(resource, request).getSkysailResponse();
        assertThat(response.getSuccess(), is(true));
        // assertThat(response.getData(), is(true));

    }

    @Test
    public void should_not_let_string_with_tags_pass() {
        myForm.add("name", "myname<script></script>");

        SkysailResponse<?> response = filterUnderTest.handle(resource, request).getSkysailResponse();
        assertThat(response.getSuccess(), is(false));
        // assertThat(response.getData(), is(true));

    }
}
