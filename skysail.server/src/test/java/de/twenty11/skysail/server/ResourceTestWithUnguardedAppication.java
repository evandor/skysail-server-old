package de.twenty11.skysail.server;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
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
        public Authenticator getAuthenticator(Context context) {
            return new DummyChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "realm");
        }

    }

    @Override
    protected AuthenticationService getAuthenticationService() {
        return new DummyAuthenticationService();
    }

}
