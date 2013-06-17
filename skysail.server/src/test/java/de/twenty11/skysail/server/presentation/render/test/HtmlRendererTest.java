package de.twenty11.skysail.server.presentation.render.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.server.presentation.render.DefaultCleaningStrategy;
import de.twenty11.skysail.server.presentation.render.HtmlRenderer;
import de.twenty11.skysail.server.presentation.render.MapTransformer;

public class HtmlRendererTest {

    private HtmlRenderer htmlRenderer;
    private HashMap<String, Object> map;

    @Before
    public void setUp() throws Exception {
        map = new HashMap<String, Object>();
        map.put(DefaultCleaningStrategy.CLASS_INDENTIFIER, "myClass");
    }

    @Test
    public void renders_string_attribute() throws Exception {
        map.put("stringKey", "stringValue");
        String tmpl = "#map.keys:{k| <tr><td style='width:300px;'><b>#k#</b></td><td>#map.(k)#</td></tr>}#\n";

        // @formatter:off
        String template = 
        "mapIteration(map) ::= \"$map.keys:{key| <tr>$withTDs(key)$$withTDs(map.(key))$</tr>}$\n\"" +
        "withTableCols(x)  ::= \"<td>$x$</td>\"";

        template = 
        "mapIteration(map) ::= \"$map.keys:{key| $asRow(map,key)$}$\n\"" +
        "asRow(map,k)      ::= \"<tr><td><b>$k$</b></td><td>$map.(k)$</td></tr>\"";

        
        STGroup group = new STGroupString("template", template, '$','$');
		htmlRenderer = new HtmlRenderer(group);
        htmlRenderer.setRendererInput(new MapTransformer(map).clean(new DefaultCleaningStrategy()).asRendererInput());
        // htmlRenderer = new HtmlRenderer("templates/test.stg", new MapTransformer(map).clean(
        // new DefaultCleaningStrategy()).asRendererInput());
        String renderedHtml = htmlRenderer.render();
        System.out.println(renderedHtml);

        assertThat(renderedHtml, containsString("stringKey"));
        assertThat(renderedHtml, containsString("stringValue"));
        assertThat(renderedHtml, not(containsString("myClass")));
    }
}
