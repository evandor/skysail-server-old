package de.twenty11.skysail.server.converter.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.representation.Representation;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.server.converter.ConverterUtils;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ConverterUtilsTest {


    @Presentation(preferred = PresentationStyle.EDIT)
    public class MyAnnotatedResource extends Resource {
        @Override
        public String getAttribute(String name) {
            return null;
        }

        @Override
        public Representation handle() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object value) {
        }
    }

    @Before
    public void setUp() {
    }

    @Test
    public void list_is_the_default_style() throws Exception {
        Resource resource = Mockito.mock(Resource.class);
        PresentationStyle style = ConverterUtils.evalPresentationStyle(resource);
        assertThat(style, is(equalTo(PresentationStyle.LIST)));
    }

    @Test
    public void annotation_is_evaluated() throws Exception {
        Resource resource = new MyAnnotatedResource();
        PresentationStyle style = ConverterUtils.evalPresentationStyle(resource);
        assertThat(style, is(equalTo(PresentationStyle.EDIT)));
    }

}
