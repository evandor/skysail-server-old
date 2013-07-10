package de.twenty11.skysail.server.internal;

import org.osgi.service.component.ComponentContext;

import de.twenty11.skysail.server.resources.LoginResource;
import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class DefaultSkysailApplication extends SkysailApplication {

    private AuthenticationService authenticationService;
    public DefaultSkysailApplication(ComponentContext componentContext) {
        super();
        if (getContext() != null) {
            setContext(getContext().createChildContext());
        }
        setComponentContext(componentContext);
        setName("default");
    }

    @Override
    protected void attach() {
        router.attach("/", DefaultResource.class);
        router.attach("/login", LoginResource.class);
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
    public void setAuthenticationService(AuthenticationService authService) {
        this.authenticationService = authService;
    }

}
