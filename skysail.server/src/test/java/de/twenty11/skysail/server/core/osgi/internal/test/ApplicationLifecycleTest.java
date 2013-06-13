package de.twenty11.skysail.server.core.osgi.internal.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.twenty11.skysail.server.core.osgi.internal.ApplicationLifecycle;
import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationLifecycleTest {

    private ApplicationLifecycle sm;

    @Before
    public void setUp() throws Exception {
        sm = new ApplicationLifecycle();
    }

    @Test
    public void starts_with_state_new() {
        assertThat(sm.getState(), is(equalTo(ApplicationState.NEW)));
    }

}
