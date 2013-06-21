package de.twenty11.skysail.server.presentation.render.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanMap;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.server.presentation.render.DefaultCleaningStrategy;
import de.twenty11.skysail.server.presentation.render.HtmlRenderer;
import de.twenty11.skysail.server.presentation.render.MapTransformer;
import de.twenty11.skysail.server.utils.IOUtils;

public class HtmlRendererTest {

    public class DummyEntity {
        public String testprop = "test";

        @Override
        public String toString() {
            return testprop;
        }
    }

    private HtmlRenderer htmlRenderer;
    private HashMap<String, Object> map;
    private String template;

    @Before
    public void setUp() throws Exception {
        map = new HashMap<String, Object>();
        map.put(DefaultCleaningStrategy.CLASS_INDENTIFIER, "myClass");

        InputStream is = this.getClass().getResourceAsStream("/templates/ListServerResource2.stg");
        template = IOUtils.convertStreamToString(is);
        is.close();
    }

    @Test
    public void renders_string_attribute() throws Exception {
        map.put("stringKey", "stringValue");

        STGroup group = new STGroupString("template", template, '$', '$');
        htmlRenderer = new HtmlRenderer(group);
        htmlRenderer.setRendererInput(new MapTransformer(map).clean(new DefaultCleaningStrategy()).asRendererInput());
        String renderedHtml = htmlRenderer.render("mapIteration");
        System.out.println(renderedHtml);

        assertThat(renderedHtml, containsString("stringKey"));
        assertThat(renderedHtml, containsString("stringValue"));
        assertThat(renderedHtml, not(containsString("myClass")));
    }

    @Test
    public void renders_default_header() throws Exception {
        BeanMap beanMap = new BeanMap(new DummyEntity());

        String template = "header(map)       ::= \"<b>$map.toString$</b>\"";
        STGroup group = new STGroupString("template", template, '$', '$');
        htmlRenderer = new HtmlRenderer(group);
        htmlRenderer.setRendererInput(new MapTransformer(beanMap).clean(new DefaultCleaningStrategy())
                .asRendererInput());

        String renderedHtml = htmlRenderer.render("header");
        System.out.println(renderedHtml);
    }
}
