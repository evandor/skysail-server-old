package de.twenty11.skysail.forms.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.server.forms.internal.SkysailApplication;

public class FormResourceTest extends BaseTest {
    @Before
    public void setUp() throws Exception {
        SkysailApplication spy = setUpRestletApplication();
        //setUpPersistence(spy);
    }
    
    @Test
    public void test() throws Exception {
        List<FormDetails> forms = getForms();
        assertThat(forms.size(), is(equalTo(0)));
    }

   
    
}
