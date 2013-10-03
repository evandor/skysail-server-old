package de.twenty11.skysail.server.core.restlet.filter.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource;
import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter;
import de.twenty11.skysail.server.core.restlet.filter.FilterResult;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
import de.twenty11.skysail.server.core.restlet.testresources.MyEntityResource;

public class ExceptionCatchingFilterTest {

    private ExceptionCatchingFilter<UniqueResultServerResource<SimpleEntity>, SimpleEntity> exceptionCatchingFilter;

    private class ExceptionThrowingFilter extends
            AbstractResourceFilter<UniqueResultServerResource<SimpleEntity>, SimpleEntity> {
        @Override
        public FilterResult doHandle(UniqueResultServerResource<SimpleEntity> resource, Request request,
                ResponseWrapper<SimpleEntity> response) {
            throw new IllegalStateException("I want to be catched by outer filter");
        }
    };

    @Before
    public void setUp() {
        exceptionCatchingFilter = new ExceptionCatchingFilter<UniqueResultServerResource<SimpleEntity>, SimpleEntity>();
    }

    @Test
    // @Profile
    public void delegates_to_next_filter_if_before_returns_continue() {

        Request request = Mockito.mock(Request.class);
        MyEntityResource resource = new MyEntityResource();

        exceptionCatchingFilter.calling(new ExceptionThrowingFilter());
        SkysailResponse<?> response = exceptionCatchingFilter.handle(resource, request).getSkysailResponse();
        assertThat(response instanceof FailureResponse, is(true));

    }
}
