package de.twenty11.skysail.server;

import org.junit.Test;

public class AgentTest {
    @Test
    public void shouldInstantiateSleepingInstance() throws InterruptedException {
 
        InstrumentationTest sleeping = new InstrumentationTest();
        sleeping.randomSleep();
    }
}
