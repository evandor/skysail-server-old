/**
 * 
 */
package de.twenty11.skysail.server.osgi.logging.memoryAppender.tests;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import skysail.server.osgi.logging.memoryAppender.MemoryAppender;

/**
 * @author Graef
 *
 */
public class MemoryAppenderTests {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link skysail.server.osgi.logging.memoryAppender.MemoryAppender#append(ch.qos.logback.classic.spi.ILoggingEvent)}.
     */
    @Test
    public void testAppendILoggingEvent() {
        MemoryAppender memApp = new MemoryAppender();
//        memApp.append()
    }

    /**
     * Test method for {@link skysail.server.osgi.logging.memoryAppender.MemoryAppender#getLogForBundle(java.lang.String)}.
     */
    @Test
    public void testGetLogForBundle() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link skysail.server.osgi.logging.memoryAppender.MemoryAppender#getLogNames()}.
     */
    @Test
    public void testGetLogNames() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link skysail.server.osgi.logging.memoryAppender.MemoryAppender#getLogs(java.lang.String)}.
     */
    @Test
    public void testGetLogs() {
        fail("Not yet implemented");
    }

}
