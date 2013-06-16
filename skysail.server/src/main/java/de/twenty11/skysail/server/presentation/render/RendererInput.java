package de.twenty11.skysail.server.presentation.render;

import java.util.Collections;
import java.util.Map;

public class RendererInput {

    private Map<String, Object> map;

    public RendererInput(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(map);
    }

}
