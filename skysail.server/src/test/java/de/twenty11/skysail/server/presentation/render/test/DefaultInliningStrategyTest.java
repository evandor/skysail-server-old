package de.twenty11.skysail.server.presentation.render.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.server.presentation.render.DefaultInliningStrategy;

public class DefaultInliningStrategyTest {

    private DefaultInliningStrategy defaultInliningStrategy;
    private HashMap<String, Object> inputMap;

    @Before
    public void setUp() throws Exception {
        inputMap = new HashMap<String, Object>();
        defaultInliningStrategy = new DefaultInliningStrategy();
    }

    @Test
    public void inlines_arrays() {
        Object[] arr = new Object[2];
        arr[0] = "test";
        arr[1] = "test2";
        inputMap.put("arrayKey", arr);

        Map<String, Object> output = defaultInliningStrategy.inline(inputMap);

        // assert
    }
}
