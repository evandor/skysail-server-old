package de.twenty11.skysail.server.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.twenty11.skysail.server.internal.Configuration;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;

public class ConfigurationTest {

    private Configuration configuration;
    private ApplicationProvider appProvider;
    private SkysailApplication app;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        appProvider = Mockito.mock(ApplicationProvider.class);
        app = Mockito.mock(SkysailApplication.class);
        Mockito.when(app.getName()).thenReturn("mockedApp");
        Mockito.when(appProvider.getApplication()).thenReturn(app);
    }

    @Test(expected = IllegalArgumentException.class)
    public void should_throw_illegal_argument_exception_when_provider_is_null() {
        configuration.addApplicationProvider(null);
    }

    @Test
    public void should_add_new_application_from_provider_with_state_new() {
        configuration.addApplicationProvider(appProvider);
        // List<Application> newApplications = configuration.getApplicationsFor(ApplicationState.NEW);
    }

}
