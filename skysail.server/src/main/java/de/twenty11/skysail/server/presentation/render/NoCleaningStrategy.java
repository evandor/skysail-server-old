package de.twenty11.skysail.server.presentation.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class NoCleaningStrategy implements CleaningStrategy {

    @Override
    public Map<String, Object> clean(Map<String, Object> inputMap) {
        Map<String, Object> result = new HashMap<String, Object>();
        for (Entry<String, Object> entry : inputMap.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}
