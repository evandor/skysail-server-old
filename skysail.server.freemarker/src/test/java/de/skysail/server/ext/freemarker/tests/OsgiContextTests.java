package de.skysail.server.ext.freemarker.tests;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import freemarker.template.Template;

/**
 * @author carsten
 *
 */
@RunWith(JUnit4TestRunner.class)
// @ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class OsgiContextTests {

    /**
     * @return the options for the testing framework.
     */
    @Configuration
    public final Option[] config() {
        return options(mavenBundle("de.evandor", "skysail.server.osgi.ext.freemarker", "0.0.1"),
        // mavenBundle("org.slf4j", "slf4j-simple", "1.6.1"),
        // mavenBundle("org.ops4j.pax.exam","pax-exam-junit","2.2.0"),
        // TODO make maven bundle
                bundle("file:///home/carsten/workspaces/skysale2/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/freemarker_2.3.18.jar"),
                // scanDir("/home/carsten/workspaces/skysale2/skysail.server.osgi.ext.freemarker"),
                junitBundles()
        // equinox().version("3.6.2")
        );
    }

    /**
     * Test that we have exactly one Configuration service.
     * @param context bundleContext
     * @throws InvalidSyntaxException should not happen
     */
    @Test
    public final void checkNumberOfConfigurationServices(final BundleContext context) throws InvalidSyntaxException {
        ServiceReference[] serviceReferences = context.getServiceReferences(
                freemarker.template.Configuration.class.getName(), null);
        assertTrue(serviceReferences.length == 1);
    }

    /**
     * @param bc bundleContext
     * @throws IOException should not happen
     */
    @Test
    public final void getDefaultTemplate(final BundleContext bc) throws IOException {
        ServiceReference serviceRef = bc.getServiceReference(freemarker.template.Configuration.class.getName());
        freemarker.template.Configuration service = (freemarker.template.Configuration) bc.getService(serviceRef);
        Template template;
        template = service.getTemplate("de.evandor.skysail.server.osgi.ext.freemarker:description.ftl");
        assertTrue(template != null);
        template = service.getTemplate("de.evandor.skysail.server.osgi.ext.freemarker:0.0.1:description.ftl");
        assertTrue(template != null);
    }
}
