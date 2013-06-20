package de.twenty11.skysail.server.presentation.render;

import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;

public class HtmlRenderer implements Renderer {

    private RendererInput rendererInput;
    private String tmpl;
    private STGroup template;

    public HtmlRenderer(STGroup template2) {
		this.template = template2;
	}

	@Override
    public String render(String declaration) {
        Map<String, Object> outputMap = determineResultMap(rendererInput.getMap());
        ST html = template.getInstanceOf(declaration);// new ST(template, '#', '#');
        html.add("map", outputMap);

        return html.render();
    }

    private Map<String, Object> determineResultMap(Map beanMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Object key : beanMap.keySet()) {
            if (beanMap.get(key) instanceof Map) {
                Map subMap = determineResultMap((Map) beanMap.get(key));
                resultMap.put((String) key, new ST("#map.keys:{k| <ul><li><b>#k#</b>: #map.(k)#</li></ul>}#\n", '#',
                        '#').add("map", subMap).render());
            } else if (beanMap.get(key) instanceof Object[]) {
                Object[] arr = (Object[]) beanMap.get(key);
                Map<String, Object> subMap = new HashMap<String, Object>();
                int i = 1;
                for (Object value : arr) {
                    subMap.put("(" + Integer.toString(i++) + ")", value);
                }
                subMap = determineResultMap(subMap);
                resultMap.put((String) key, new ST("#map.keys:{k| <ul><li><b>#k#</b>: #map.(k)#</li></ul>}#\n", '#',
                        '#').add("map", subMap).render());
            } else {
                resultMap.put((String) key, beanMap.get(key));
            }
        }
        return resultMap;
    }

    public void setRendererInput(RendererInput rendererInput) {
        this.rendererInput = rendererInput;
    }

}
