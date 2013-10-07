package de.twenty11.skysail.server;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.Authorizer;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.security.AuthorizationService;

public class ResourceTestWithUnguardedAppication<T extends SkysailApplication> extends ResourceTestWithApplication<T> {

    private AuthenticationService dummyAuthenticationService = new DummyAuthenticationService();
    private static AuthorizationService dummyAuthorizationService = new DummyAuthorizationService();

    public static class DummyAuthorizationService implements AuthorizationService {

        private String password;
        private String username;

        @Override
        public Authorizer getRoleAuthorizer(final String roleName) {
            return new Authorizer() {

                @Override
                protected boolean authorize(Request request, Response response) {
                    Subject subject = SecurityUtils.getSubject();
                    subject.login(new UsernamePasswordToken(username, password));
                    return password.equals(username.toLowerCase());
                }
            };
        }

        public void setUsernamePassword(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

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

        @Override
        public void logout() {
        }

    }

    @Override
    protected AuthenticationService getAuthenticationService() {
        return dummyAuthenticationService;
    }

    @Override
    protected AuthorizationService getAuthorizationService() {
        return dummyAuthorizationService;
    }

    public static DummyAuthorizationService getDummyAuthorizationService() {
        return (DummyAuthorizationService) dummyAuthorizationService;
    }

}
