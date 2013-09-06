package de.twenty11.skysail.server;

/**
 * http://blog.javabenchmark.org/2013/05/java-instrumentation-tutorial.html
 *
 */
public class InstrumentationTest {
    public void randomSleep() throws InterruptedException {
        
        // randomly sleeps between 500ms and 1200s
        long randomSleepDuration = (long) (500 + Math.random() * 700);
        System.out.printf("Sleeping for %d ms ..\n", randomSleepDuration);
        Thread.sleep(randomSleepDuration);
    }
}
