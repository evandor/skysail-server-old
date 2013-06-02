package de.twenty11.skysail.server.core.restlet;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.Restlet;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.server.core.restlet.RouteBuilder;

public class RouteBuilderTest {

    private RouteBuilder routeBuilder;

    @Before
    public void setUp() {
        routeBuilder = new RouteBuilder("path", ServerResource.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_path_gives_execption() {
        new RouteBuilder(null, ServerResource.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void null_class_gives_execption() {
        new RouteBuilder("", (Restlet) null);
    }

    @Test
    public void is_initialized_with_proper_values() {
        assertThat(routeBuilder.getPathTemplate(), is(equalTo("path")));
        assertThat(routeBuilder.getTargetClass().getName(), is(equalTo(ServerResource.class.getName())));
        assertThat(routeBuilder.isVisible(), is(true));
        assertThat(routeBuilder.getText(), is(nullValue()));
    }

    @Test
    public void has_the_proper_visibility() {
        routeBuilder.setVisible(false);
        assertThat(routeBuilder.isVisible(), is(false));
    }

    @Test
    public void has_the_proper_text() {
        routeBuilder.setText("text");
        assertThat(routeBuilder.getText(), is(equalTo("text")));
    }

    @Test
    public void shows_relevant_info_in_toString() {
        String toString = routeBuilder.setText("text").toString();
        assertThat(toString, containsString("path"));
    }

}
