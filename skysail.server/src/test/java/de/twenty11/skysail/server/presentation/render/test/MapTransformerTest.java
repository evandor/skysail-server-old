package de.twenty11.skysail.server.presentation.render.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.presentation.render.DefaultCleaningStrategy;
import de.twenty11.skysail.server.presentation.render.MapTransformer;
import de.twenty11.skysail.server.presentation.render.RendererInput;

public class MapTransformerTest {

    private Map<String, Object> map = new HashMap<String, Object>();

    @Before
    public void setUp() throws Exception {
        map.put(DefaultCleaningStrategy.CLASS_INDENTIFIER, "classValue");
        map.put("stringValue", "someString");
        map.put("longValue", 7L);
    }

    @Test
    public void testName() throws Exception {
        MapTransformer transformer = new MapTransformer(map);
        RendererInput ri = transformer.clean(new DefaultCleaningStrategy()).asRendererInput();
        assertThat(ri.getMap().get(DefaultCleaningStrategy.CLASS_INDENTIFIER), is(nullValue()));
        assertThat(ri.getMap().size(), is(greaterThan(1)));
    }

}
