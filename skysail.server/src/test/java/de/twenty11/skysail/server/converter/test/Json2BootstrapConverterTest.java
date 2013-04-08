package de.twenty11.skysail.server.converter.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Restlet;
import org.restlet.data.Reference;
import org.restlet.resource.Resource;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.restlet.util.RouteList;

import de.twenty11.skysail.server.converter.Breadcrumb;
import de.twenty11.skysail.server.converter.Json2BootstrapConverter;
import de.twenty11.skysail.server.restlet.SkysailApplication;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class Json2BootstrapConverterTest {

    private Json2BootstrapConverter converter;
    private Resource resource;
    private SkysailApplication myApplication;
    private Reference myReference;
    private RouteList routes;
    private Router router;

    @Before
    public void setUp() throws Exception {
        converter = new Json2BootstrapConverter();
        resource = Mockito.mock(Resource.class);
        myApplication = Mockito.mock(SkysailApplication.class);
        myReference = Mockito.mock(Reference.class);
        router = Mockito.mock(Router.class);
        routes = new RouteList();
        Mockito.when(resource.getApplication()).thenReturn(myApplication);
        Mockito.when(resource.getReference()).thenReturn(myReference);
        Mockito.when(resource.getRootRef()).thenReturn(myReference);
        Mockito.when(myApplication.getRoutes()).thenReturn(routes);
    }

    @Test
    public void calculates_correct_breadcrumb_for_emptyRoute() {
        addRoute("");
        Mockito.when(myReference.getSegments()).thenReturn(Arrays.<String> asList("abc"));

        List<Breadcrumb> breadcrumbs = converter.getBreadcrumbList(resource);

        assertThat(breadcrumbs.size(), is(equalTo(2)));
    }

    @Test
    public void calculates_correct_osgimonitor_breadcrumb_for_bundlePath() {
        addRoute("");
        addRoute("/");
        addRoute("/bundles");
        addRoute("/bundles/");
        Mockito.when(myReference.getSegments()).thenReturn(Arrays.<String> asList("osgimonitor", "bundles"));

        List<Breadcrumb> breadcrumbs = converter.getBreadcrumbList(resource);

        assertThat(breadcrumbs.size(), is(equalTo(3)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void toObject_throws_unsupportedOperationException() throws Exception {
        converter.toObject(null, null, resource);
    }

    private void addRoute(String uriTemplate) {
        routes.add(new TemplateRoute(router, uriTemplate, new Restlet() {
        }));
    }




}
