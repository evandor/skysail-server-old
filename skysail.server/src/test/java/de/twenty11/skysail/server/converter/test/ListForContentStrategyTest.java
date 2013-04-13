package de.twenty11.skysail.server.converter.test;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.converter.ListForContentStrategy;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ListForContentStrategyTest {

    private ListForContentStrategy strategy;

    @Before
    public void setUp() {
        strategy = new ListForContentStrategy();
    }

    @Test
    @Ignore
    public void test() {
        Object responseObject = null;
        SkysailResponse<List<?>> skysailResponse = Mockito.mock(SkysailResponse.class);
        String html = strategy.createHtml("", responseObject, skysailResponse);
        assertThat(html.length(),is(equalTo(12)));
    }
}
