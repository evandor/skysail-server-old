package de.twenty11.skysail.server.internal;

import de.twenty11.skysail.server.resources.LoginResource;
import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.restlet.SkysailApplication;

import org.osgi.service.component.ComponentContext;

public class DefaultSkysailApplication extends SkysailApplication {

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
}
