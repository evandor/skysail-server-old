package de.skysail.server.osgi.logging.memoryAppender.tests;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author carsten
 * 
 */
@RunWith(JUnit4TestRunner.class)
// @ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class OsgiContextTests {

    private static Logger logger = LoggerFactory.getLogger(OsgiContextTests.class);

    /**
     * @return the options for the testing framework.
     */
    @Configuration
    public final Option[] config() {
        return options(mavenBundle("de.evandor", "skysail.server.osgi.logging.memoryAppender", "0.0.1-SNAPSHOT"),
                mavenBundle("org.slf4j", "slf4j-api", "1.6.1"),
                //mavenBundle("de.evandor","skysail.server.osgi.logback.config","0.4.0").noStart(),
                mavenBundle("de.evandor","skysail.server.osgi.logging.osgi-over-slf4j","0.0.1-SNAPSHOT"),
                mavenBundle("ch.qos.logback", "logback-core", "0.9.29").startLevel(3),
                mavenBundle("ch.qos.logback", "logback-classic", "0.9.29").startLevel(3),
                mavenBundle("org.eclipse.equinox","log","1.2.100.v20100503"),
                // mavenBundle("org.ops4j.pax.exam","pax-exam-junit","2.2.0"),
                // TODO make maven bundle
                // bundle("file:///home/carsten/workspaces/skysale2/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/freemarker_2.3.16.jar"),
                // scanDir("/home/carsten/workspaces/skysale2/skysail.server.osgi.ext.freemarker"),
                junitBundles()
        // equinox().version("3.6.2")
        );
//        return options(
//                scanDir("C:/workspaces/skysail/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/skysail/").filter("*.jar"),
//                junitBundles()
//        // equinox().version("3.6.2")
//        );
    }

    /**
     * @param bc
     *            bundleContext
     * @throws IOException
     *             should not happen
     */
    @Test
    public final void testLimitedQueue(final BundleContext bc) throws IOException {
    }

}
