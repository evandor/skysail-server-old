package de.twenty11.skysail.server.presentation.render.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.junit.Before;
import org.junit.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.server.utils.IOUtils;

import static org.hamcrest.Matchers.containsString;

import static org.junit.Assert.assertThat;

public class HtmlRendererTest {

    public class Entity {

        private String name;

        public Entity(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }

    }

    private List<BeanMap> list;
    private STGroup group;

    @Before
    public void setUp() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/templates/ListServerResource2Test.stg");
        String template = IOUtils.convertStreamToString(is);
        is.close();
        group = new STGroupString("template", template, '$', '$');

        list = new ArrayList<BeanMap>();
        list.add(new BeanMap(new Entity("first")));
        list.add(new BeanMap(new Entity("second")));

    }

    @Test
    public void renders_listResource_as_accordion() throws Exception {
 
        ST accordion = group.getInstanceOf("accordion");
        accordion.add("list", list);
        //accordion.inspect();

        String renderedHtml = accordion.render();
        assertThat(renderedHtml, containsString("<accordion>"));
        assertThat(renderedHtml, containsString("</accordion>"));
        
        assertThat(renderedHtml, containsString("<asRow>name: first</asRow>"));
    }

}
