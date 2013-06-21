package de.twenty11.skysail.server.presentation.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultCleaningStrategy implements CleaningStrategy {

    public static final String CLASS_INDENTIFIER = "class";

    @Override
    public Map<String, Object> clean(Map<String, Object> inputMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Entry<String, Object> entry : inputMap.entrySet()) {
            // if (CLASS_INDENTIFIER.equals(entry.getKey())) {
            // continue;
            // }
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
