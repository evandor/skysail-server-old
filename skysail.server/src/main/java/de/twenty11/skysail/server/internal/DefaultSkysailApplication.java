package de.twenty11.skysail.server.internal;

import org.osgi.service.component.ComponentContext;

import de.twenty11.skysail.server.resources.DefaultResource;
import de.twenty11.skysail.server.resources.LoginResource;
import de.twenty11.skysail.server.resources.LogoutResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class DefaultSkysailApplication extends SkysailApplication {

    public static final String LOGIN_PATH = "/login";
    public static final String LOGOUT_PATH = "/logout";

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
        router.attach(LOGIN_PATH, LoginResource.class);
        router.attach(LOGOUT_PATH, LogoutResource.class);
    }

    @Override
    public void setAuthenticationService(AuthenticationService authService) {
        super.setAuthenticationService(authService);
    }

}
