package de.twenty11.skysail.server.restlet.test;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.server.restlet.RouteBuilder;
import de.twenty11.skysail.server.restlet.SkysailRouter;

public class SkysailRouterTest {

    private SkysailRouter skysailRouter;

    @Before
    public void setUp() throws Exception {
        skysailRouter = new SkysailRouter(null);
    }

    @Test
    public void can_attach_routeBuilder() {
        RouteBuilder routeBuilder = new RouteBuilder("path", ServerResource.class).setText("hi");
        skysailRouter.attach(routeBuilder);
        assertThat(skysailRouter.getRoutes().size(), is(equalTo(1)));
        assertThat(skysailRouter.getRouteBuilder("path").getText(), is(equalTo("hi")));
        assertThat(skysailRouter.getTemplatePathForResource(ServerResource.class),is(equalTo("path")));
    }

}
