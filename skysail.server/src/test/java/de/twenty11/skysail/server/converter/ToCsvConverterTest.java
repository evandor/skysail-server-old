package de.twenty11.skysail.server.converter;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.representation.Representation;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.PresentableHeader;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.common.responses.SuccessResponse;

public class ToCsvConverterTest {

    public class SomeDetails implements Presentable {

        private String one;
        private int two;
        private String three;

        public SomeDetails(String string, int i, String string2) {
            this.one = string;
            this.two = i;
            this.three = string2;
        }

        @Override
        @JsonIgnore
        public PresentableHeader getHeader() {
            return null;
        }

        @Override
        @JsonIgnore
        public Map<String, Object> getContent() {
            Map<String, Object> result = new LinkedHashMap<String, Object>();
            result.put("one", this.one);
            result.put("two", this.two);
            result.put("three", this.three);
            return result;
        }

    }

    private ToCsvConverter toCsvConverter;

    @Before
    public void setUp() throws Exception {
        toCsvConverter = new ToCsvConverter();
    }
    
    @Test
    public void gives_no_data_representation_for_empty_input() {
        SkysailResponse<SomeDetails> source = new SkysailResponse<SomeDetails>();
        Resource resource = Mockito.mock(Resource.class);
        Representation representation = toCsvConverter.toRepresentation(source, null, resource);
        assertThat(representation.toString(), is(equalTo("no data")));
    }

    @Test
    public void gives_no_data_representation_for_failure_response_input() {
        FailureResponse<SomeDetails> source = new FailureResponse<SomeDetails>("failure!");
        Resource resource = Mockito.mock(Resource.class);
        Representation representation = toCsvConverter.toRepresentation(source, null, resource);
        assertThat(representation.toString(), is(equalTo("failure!")));
    }

    @Test
    public void gives_proper_csv_representation_for_one_line_input_input() {
        SomeDetails data = new SomeDetails("one", 2, "three");
        SuccessResponse<SomeDetails> source = new SuccessResponse<SomeDetails>(data);
        Resource resource = Mockito.mock(Resource.class);
        Representation representation = toCsvConverter.toRepresentation(source, null, resource);
        assertThat(representation.toString(), is(equalTo("one,2,three\n")));
    }

}
