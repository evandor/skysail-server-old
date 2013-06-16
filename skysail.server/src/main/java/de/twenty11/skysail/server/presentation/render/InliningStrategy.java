package de.twenty11.skysail.server.presentation.render;

import java.util.Map;

public interface InliningStrategy {

    Map<String, Object> inline(Map<String, Object> inputMap);
}
