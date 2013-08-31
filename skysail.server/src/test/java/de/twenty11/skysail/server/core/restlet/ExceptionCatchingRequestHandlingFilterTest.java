package de.twenty11.skysail.server.core.restlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.SkysailRequestHandlingFilterTest.Entity;
import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;

public class ExceptionCatchingRequestHandlingFilterTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void delegates_to_next_filter_if_before_returns_continue() {
        SkysailRequestHandlingFilter<Entity> exceptionThrowingFilter = new SkysailRequestHandlingFilter<Entity>() {
            @Override
            protected int doHandle(SkysailServerResource<Entity> resource, Request request,
                    ResponseWrapper<Entity> response) {
                throw new IllegalStateException("I want to be catched by outer filter");
            }
        };
        SkysailRequestHandlingFilter<Entity> outer = new ExceptionCatchingRequestHandlingFilter<Entity>()
                .calling(exceptionThrowingFilter);

        Request request = Mockito.mock(Request.class);
        ListServerResource resource = new MyListResource();// .mock(ListServerResource.class);

        SkysailResponse<?> response = outer.handle(resource, request).getSkysailResponse();
        assertThat(response instanceof FailureResponse, is(true));

    }
}
