package de.skysail.server.osgi.logging.memoryAppender.tests;

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import skysail.server.osgi.logging.memoryAppender.MemoryAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

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
        LoggerContext lc = new LoggerContext();
        ch.qos.logback.classic.Logger testLogger1 = lc.getLogger("testLogger1");
        //org.ops4j.pax.logging.slf4j.Slf4jLogger root = (org.ops4j.pax.logging.slf4j.Slf4jLogger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        //Appender<ILoggingEvent> appender = root.
//        for (Logger l : lc.getLoggerList()) {
//            Iterator ite = l.
//            while(ite.hasNext()) {
//              Object o = ite.next();
//              if(o instanceof CyclicBufferAppender) {
//                // dump
//              }
//            }
//         }

//        Logger rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
//        rootLogger.get
        
        
        MemoryAppender logAppender = new MemoryAppender();
        logAppender.setLogBufferSize(3);
        
        createLogEvent (logAppender, testLogger1, "test0");
        printValues(logAppender);

        createLogEvent (logAppender, testLogger1, "test1");
        printValues(logAppender);

        createLogEvent (logAppender, testLogger1, "test2");
        printValues(logAppender);

        createLogEvent (logAppender, testLogger1, "test3");
        printValues(logAppender);

        createLogEvent (logAppender, testLogger1, "test4");
        printValues(logAppender);

        createLogEvent (logAppender, testLogger1, "test5");
        printValues(logAppender);
    }

    private void printValues(MemoryAppender logAppender) {
        Set<String> logNames = logAppender.getLogNames();
        System.out.println(logNames);
        for (String name : logNames) {
            Set<String> logs = logAppender.getLogs(name);
            //System.out.println(logs);
            //Collections.sort(logs);
            List<String> lines = new ArrayList<String>();
            for (String line : logs) {
                lines.add(line);
            }
            Collections.sort(lines);
            for (String line : lines) {
                System.out.println(line);
            }
            
        }
    }

    private void createLogEvent(MemoryAppender logAppender, ch.qos.logback.classic.Logger logger, String msg) {
        LoggingEvent event = new LoggingEvent("fqcn", logger, Level.INFO, msg, null, null);
        logAppender.append(event);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
