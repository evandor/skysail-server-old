package de.twenty11.skysail.server.presentation.render;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.server.utils.IOUtils;

public class HtmlRenderer implements Renderer {

    private RendererInput rendererInput;
    private String tmpl;
    private STGroupString template;

    // public HtmlRenderer(String tmpl, RendererInput rendererInput) {
    // this.tmpl = tmpl;
    // this.rendererInput = rendererInput;
    //
    // InputStream resourceAsStream = this.getClass().getResourceAsStream(tmpl);
    // String templateGroup = IOUtils.convertStreamToString(resourceAsStream);
    // System.out.println(templateGroup);
    //
    // STGroup g = new STGroupString(tmpl, templateGroup, '%', '%');
    // ST st = g.getInstanceOf("mapIteration");
    // st.add("map", new BeanMap());
    // String result = st.render();
    // System.out.println(result);
    // }
    //
    public HtmlRenderer(String relativeTemplateFilePath) {
        Class<? extends HtmlRenderer> rendererClass = this.getClass();
        InputStream resourceAsStream = rendererClass.getResourceAsStream(tmpl);
        String templateGroup = IOUtils.convertStreamToString(resourceAsStream);
        System.out.println(templateGroup);
        template = new STGroupString(tmpl, templateGroup, '%', '%');
    }

    @Override
    public String render() {
        Map<String, Object> outputMap = determineResultMap(rendererInput.getMap());
        ST html = new ST(tmpl, '#', '#');
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
