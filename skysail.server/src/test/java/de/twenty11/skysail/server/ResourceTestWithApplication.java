package de.twenty11.skysail.server;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mockito.Mockito;
import org.restlet.Application;
import org.restlet.Context;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.security.AuthorizationService;

public abstract class ResourceTestWithApplication<T extends SkysailApplication> {

    private static final String TESTDB_URL = "jdbc:derby:etc/skysailDerbyTestDb;create=true";

    private static final String TESTDB_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final String TESTDB_PASSWORD = "skysail";

    private static final String TESTDB_USER = "skysail";

    public static final int TEST_PORT = 8182;

    protected T application;

    protected abstract AuthenticationService getAuthenticationService();

    protected abstract AuthorizationService getAuthorizationService();

    protected SkysailApplication setUpApplication(T application) {
        application.setContext(new Context());
        application.setAuthenticationService(getAuthenticationService());
        application.setAuthorizationService(getAuthorizationService());
        application.getInboundRoot();
        return application;
    }

    protected T setUpMockedApplication(T freshApplicationInstance) throws ClassNotFoundException {
        application = freshApplicationInstance;
        application.setContext(new Context());
        application.setAuthenticationService(getAuthenticationService());
        application.setAuthorizationService(getAuthorizationService());
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

        props.put("javax.persistence.jdbc.driver", TESTDB_DRIVER);
        props.put("javax.persistence.jdbc.url", TESTDB_URL);
        props.put("javax.persistence.jdbc.user", TESTDB_USER);
        props.put("javax.persistence.jdbc.password", TESTDB_PASSWORD);
        props.put("eclipselink.ddl-generation", "drop-and-create-tables");

        return Persistence.createEntityManagerFactory(puName, props);
    }

    public DataSource getTestDbDataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUsername(TESTDB_USER);
        basicDataSource.setPassword(TESTDB_PASSWORD);
        basicDataSource.setUrl(TESTDB_URL);
        basicDataSource.setDriverClassName(TESTDB_DRIVER);
        return basicDataSource;
    }

}
