package de.twenty11.skysail.server.core.restlet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;

public class SkysailRequestHandlingFilterTest {

    public class Entity {

    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void delegates_to_next_filter_if_before_returns_continue() {
        ResourceFilter<Entity> next = new ResourceFilter<Entity>() {
            @Override
            protected int doHandle(SkysailServerResource<Entity> resource, Request request,
                    ResponseWrapper<Entity> response) {
                return super.doHandle(resource, request, response);
            }
        };
        ResourceFilter<Entity> first = new ResourceFilter<Entity>(next) {
        };

        Request request = Mockito.mock(Request.class);
        first.doHandle(null, request, null);

    }

}
