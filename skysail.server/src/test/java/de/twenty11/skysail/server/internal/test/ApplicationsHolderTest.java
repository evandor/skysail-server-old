package de.twenty11.skysail.server.internal.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Application;

import de.twenty11.skysail.server.core.osgi.internal.ApplicationState;
import de.twenty11.skysail.server.internal.ApplicationsHolder;

public class ApplicationsHolderTest {

    private ApplicationsHolder applicationsHolder;
    private Application application;

    @Before
    public void setUp() throws Exception {
        applicationsHolder = new ApplicationsHolder();
        application = Mockito.mock(Application.class);
        Mockito.when(application.getName()).thenReturn("mockedApp");
        applicationsHolder.add(application);
    }

    @Test
    public void test_adding_a_new_application() throws Exception {
        assertThat(applicationsHolder.getApplicationsInState(ApplicationState.NEW).size(), is(1));
    }

}
