package de.twenty11.skysail.server;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
import org.mockito.Mockito;
import org.restlet.Application;
import org.restlet.Context;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceTestWithApplication<T extends SkysailApplication> {

    public static final int TEST_PORT = 8182;

    private T application;

    protected abstract AuthenticationService getAuthenticationService();

    protected SkysailApplication setUpApplication(T application) {
        application.setContext(new Context());
        application.setAuthenticationService(getAuthenticationService());
        application.getInboundRoot();
        return application;
    }

    protected T setUpMockedApplication(T freshApplicationInstance) throws ClassNotFoundException {
        application = freshApplicationInstance;
        application.setContext(new Context());
        application.setAuthenticationService(getAuthenticationService());
        T spy = Mockito.spy(application);
        Application.setCurrent(spy);
        application.getInboundRoot();
        return spy;
    }

    protected String requestUrlFor(String resource) {
        return "http://localhost:" + TEST_PORT + resource;
    }
    protected EntityManagerFactory getEmfForTests(String puName) {
        Map<String, Object> props = new HashMap<String, Object>();

        props.put("javax.persistence.jdbc.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        props.put("javax.persistence.jdbc.url", "jdbc:derby:etc/skysailDerbyTestDb;create=true");
        props.put("javax.persistence.jdbc.user", "skysail");
        props.put("javax.persistence.jdbc.password", "skysail");
        props.put("eclipselink.ddl-generation", "drop-and-create-tables");

        return Persistence.createEntityManagerFactory(puName, props);
    }

}
