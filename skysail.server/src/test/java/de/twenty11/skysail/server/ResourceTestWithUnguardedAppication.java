package de.twenty11.skysail.server;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.mockito.Mockito;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.routing.Filter;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class ResourceTestWithUnguardedAppication<T extends SkysailApplication> {

    public static final int TEST_PORT = 8182;

    private T application;

    public class DummyChallengeAuthenticator extends ChallengeAuthenticator {

        public DummyChallengeAuthenticator(Context context, ChallengeScheme challengeScheme, String realm) {
            super(context, challengeScheme, realm);
        }

        @Override
        public Verifier getVerifier() {
            return new Verifier() {

                @Override
                public int verify(Request request, Response response) {
                    return Verifier.RESULT_VALID;
                }
            };
        }
    }

    private class DummyAuthenticationService implements AuthenticationService {

        @Override
        public void logout() {
        }

        @Override
        public void login(String username, String password) {
            // handle izzy and linus
        }

        @Override
        public Authenticator getAuthenticator(Context context) {
            return new DummyChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "realm");
        }

        @Override
        public Filter getRestletShiroFilter(Context context) {
            return new Filter() {
            };
        }

    }

    protected SkysailApplication setUpApplication(T application) {
        application.setContext(new Context());
        application.setAuthenticationService(new DummyAuthenticationService());
        application.getInboundRoot();
        return application;
    }

    protected T setUpMockedApplication(T freshApplicationInstance) throws ClassNotFoundException {
        application = freshApplicationInstance;
        application.setContext(new Context());
        application.setAuthenticationService(new DummyAuthenticationService());
        T spy = Mockito.spy(application);
        Application.setCurrent(spy);
        application.getInboundRoot();
        return spy;
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

    protected String requestUrlFor(String resource) {
        return "http://localhost:" + TEST_PORT + resource;
    }
}
