package de.twenty11.skysail.server.restlet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import com.google.common.base.Joiner;

import de.twenty11.skysail.server.restlet.SkysailApplication;

public class SkysailApplicationTest {

    // @formatter:off
    static final String EXAMPLE = Joiner.on('\n').join(
            "<h1 id='foo'>Header</h1>",
            "<p onclick='alert(42)'>Paragraph 1<script>evil()</script></p>",
            ("<p><a href='java\0script:bad()'>Click</a> <a href='foo.html'>me</a>"
             + " <a href='http://outside.org/'>out</a></p>"),
            ("<p><img src=canary.png alt=local-canary>" +
             "<img src='http://canaries.org/canary.png'></p>"),
            "<p><b style=font-size:bigger>Fancy</b> with <i><b>soupy</i> tags</b>.",
            "<p style='color: expression(foo()); text-align: center;",
            "          /* direction: ltr */; font-weight: bold'>Stylish Para 1</p>",
            "<p style='color: red; font-weight; expression(foo());",
            "          direction: rtl; font-weight: bold'>Stylish Para 2</p>",
            "");
    
    private SkysailApplication skysailApplication;

    @Before
    public void setUp() {
        skysailApplication = new SkysailApplication("test") {
            @Override
            protected void attach() {}
        };
    }
    
    @Test
    public void testTextFilter() throws Exception {
        assertEquals(
                Joiner.on('\n').join(
                        "Header", 
                        "Paragraph 1", 
                        "Click me out", 
                        "", 
                        "Fancy with soupy tags.",
                        "Stylish Para 1", 
                        "Stylish Para 2", ""), 
                apply(new HtmlPolicyBuilder()));
    }

    private static String apply(HtmlPolicyBuilder b) throws Exception {
        return apply(b, EXAMPLE);
    }

    private static String apply(HtmlPolicyBuilder b, String src) throws Exception {
        StringBuilder sb = new StringBuilder();
        HtmlSanitizer.Policy policy = b.build(HtmlStreamRenderer.create(sb, new Handler<String>() {
            @Override
            public void handle(String x) {
                fail(x);
            }
        }));
        HtmlSanitizer.sanitize(src, policy);
        return sb.toString();
    }

}
