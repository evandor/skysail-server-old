package de.twenty11.skysail.server.core.restlet;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.resource.ServerResource;
import org.restlet.security.Authorizer;

import de.twenty11.skysail.server.security.AuthorizationService;

public class SkysailRouterTest {

    private SkysailRouter skysailRouter;
    private AuthorizationService authService = Mockito.mock(AuthorizationService.class);

    @Before
    public void setUp() throws Exception {
        skysailRouter = new SkysailRouter(null, authService);
    }

    @Test
    public void can_attach_routeBuilder() {
        Authorizer authorizer = Mockito.mock(Authorizer.class);
        Mockito.when(authService.getRoleAuthorizer(null)).thenReturn(authorizer);

        RouteBuilder routeBuilder = new RouteBuilder("path", ServerResource.class).setText("hi");
        skysailRouter.attach(routeBuilder);
        assertThat(skysailRouter.getRoutes().size(), is(equalTo(1)));
        assertThat(skysailRouter.getRouteBuilder("path").getText(), is(equalTo("hi")));
        assertThat(skysailRouter.getTemplatePathForResource(ServerResource.class), is(equalTo("path")));
    }

}
