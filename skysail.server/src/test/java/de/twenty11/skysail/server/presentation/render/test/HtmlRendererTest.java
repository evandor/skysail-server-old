package de.twenty11.skysail.server.presentation.render.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.presentation.render.HtmlRenderer;
import de.twenty11.skysail.server.presentation.render.Renderer;
import de.twenty11.skysail.server.presentation.render.RendererInput;

public class HtmlRendererTest {

    private Renderer htmlRenderer;

    @Before
    public void setUp() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        RendererInput rendererInput = new RendererInput(map);
        htmlRenderer = new HtmlRenderer(rendererInput);
    }

    @Test
    public void testName() throws Exception {
        String renderedHtml = htmlRenderer.render();
        assertThat(renderedHtml, is(equalTo("")));
    }
}
