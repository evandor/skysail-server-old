package de.twenty11.skysail.server.converter.test;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.responses.SuccessResponse;
import de.twenty11.skysail.server.converter.Json2BootstrapConverter;

public class Json2BootstrapConverterTest {

    public class MyType {

    }

    private Json2BootstrapConverter converter;
    private SuccessResponse<MyType> response;

    @Before
    public void setUp() throws Exception {
        converter = new Json2BootstrapConverter();
        MyType data = new MyType();
        response = new SuccessResponse<MyType>(data);
    }

    @Test
    public void convertMinimal() {
        // String json = "{}";
        // String html = converter.jsonToHtml(response, null);
        // String[] lines = html.split("\\n");
        // assertThat(lines[0], is(equalTo("<div id=\"json\">")));
        // assertThat(lines[1], is(equalTo("{")));
        // assertThat(lines[2], is(equalTo("  <ul class=\"obj collapsible\">")));
        // assertThat(lines[3], is(equalTo("  </ul>")));
        // assertThat(lines[4], is(equalTo("}")));
        // assertThat(lines[5], is(equalTo("</div>")));

    }




}
