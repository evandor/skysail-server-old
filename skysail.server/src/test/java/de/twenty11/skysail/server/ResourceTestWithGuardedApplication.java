package de.twenty11.skysail.server;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
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

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class ResourceTestWithGuardedApplication<T extends SkysailApplication> extends ResourceTestWithApplication {

    @Override
    protected AuthenticationService getAuthenticationService() {
        return null;//new Shiro;
    }
}
