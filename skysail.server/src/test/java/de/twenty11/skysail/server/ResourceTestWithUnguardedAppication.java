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

public class ResourceTestWithUnguardedAppication<T extends SkysailApplication> extends ResourceTestWithApplication {

    private class DummyChallengeAuthenticator extends ChallengeAuthenticator {

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

    @Override
    protected AuthenticationService getAuthenticationService() {
        return new DummyAuthenticationService();
    }



}
