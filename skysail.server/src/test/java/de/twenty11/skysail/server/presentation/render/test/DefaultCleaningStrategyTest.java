package de.twenty11.skysail.server.presentation.render.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.twenty11.skysail.server.presentation.render.DefaultCleaningStrategy;

public class DefaultCleaningStrategyTest {

    private DefaultCleaningStrategy defaultCleaningStrategy;
    private HashMap<String, Object> inputMap;

    @Before
    public void setUp() throws Exception {
        defaultCleaningStrategy = new DefaultCleaningStrategy();
        inputMap = new HashMap<String, Object>();
    }

    @Test
    @Ignore
    public void cleans_class_element() {
        inputMap.put(DefaultCleaningStrategy.CLASS_INDENTIFIER, "someValue");
        inputMap.put("other", "someOtherValue");

        Map<String, Object> outputMap = defaultCleaningStrategy.clean(inputMap);

        assertThat(outputMap.size(), is(1));
        assertThat(outputMap.get(DefaultCleaningStrategy.CLASS_INDENTIFIER), is(nullValue()));
    }
}
