package de.twenty11.skysail.server.restlet;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.EventAdmin;
import org.owasp.html.HtmlPolicyBuilder;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.resource.ServerResource;
import org.restlet.security.Authenticator;
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.config.ServerConfiguration;
import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.core.restlet.SkysailRouter;
import de.twenty11.skysail.server.internal.Blocker;
import de.twenty11.skysail.server.restlet.filter.Tracer;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.security.AuthorizationService;

/**
 * 
 * Concurrency note from parent class: instances of this class or its subclasses can be invoked by several threads at
 * the same time and therefore must be thread-safe. You should be especially careful when storing state in member
 * variables.
 * 
 * @author carsten
 * 
 */
public abstract class SkysailApplication extends Application {

    /**
     * slf4j based logger implementation.
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final MediaType SKYSAIL_HTMLFORM_MEDIATYPE = MediaType.register("htmlform", "HTML Form document");

    /**
     * the restlet router.
     */
    protected volatile SkysailRouter router;

    // private Verifier verifier = new MapVerifier();

    private ServerConfiguration config;

    private ComponentContext componentContext;

    private BundleContext bundleContext;

    private AuthenticationService authenticationService;

    private HtmlPolicyBuilder noHtmlPolicyBuilder = new HtmlPolicyBuilder();

    private AuthorizationService authorizationService;

    private EventAdmin eventAdmin;

    private String home;

    abstract protected void attach();

    public SkysailApplication(String home) {
        logger.info("Instanciating new Skysail Application '{}'", this.getClass().getSimpleName());
        this.home = home;
    }

    public BundleContext getBundleContext() {
        if (this.bundleContext != null) {
            return bundleContext;
        }
        return componentContext != null ? componentContext.getBundleContext() : null;
    }

    protected void setHome(String home) {
        this.home = home;
    }

    public String getHome() {
        return home;
    }

    @Override
    public Restlet createInboundRoot() {

        logger.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(getContext(), getAuthorizationService());
        // router.setDefaultMatchingQuery(true);

        getMetadataService().addExtension("htmlform", SKYSAIL_HTMLFORM_MEDIATYPE);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        // getConnectorService().getClientProtocols().add(Protocol.HTTPS);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);

        attach();

        Timer timer = new Timer(getContext());
        Blocker blocker = new Blocker(getContext());
        Tracer tracer = new Tracer(getContext(), getEventAdmin());
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        blocker.setNext(originalRequestFilter);
        originalRequestFilter.setNext(router);

        Authenticator guard = getAuthenticationService().getAuthenticator(getContext());

        // tracer -> guard -> timer -> blocker -> originalRequest -> router
        timer.setNext(blocker);
        guard.setNext(timer);
        tracer.setNext(guard);
        return tracer;
    }

    public void attachToRouter(String key, Class<? extends ServerResource> executor) {
        router.attach(key, executor);
    }

    public void detachFromRouter(Class<?> executor) {
        router.detach(executor);

    }

    public RouteList getRoutes() {
        return router.getRoutes();
    }

    public Map<String, RouteBuilder> getSkysailRoutes() {
        return router.getRouteBuilders();
    }

    @Override
    public Application getApplication() {
        return this;
    }

    public String getLinkTo(Reference reference, Class<? extends ServerResource> cls) {
        String relativePath = router.getTemplatePathForResource(cls);
        return reference.toString() + relativePath;
    }

    public void setServerConfiguration(ServerConfiguration config) {
        this.config = config;
    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public void unsetEventAdmin() {
        this.eventAdmin = null;
    }

    public String getConfigForKey(String key) {
        return this.config.getConfigForKey(key);
    }

    protected void activate(ComponentContext componentContext) throws ConfigurationException {
        logger.info("Activating Application {}", this.getClass().getName());
        this.componentContext = componentContext;
    }

    protected void deactivate(ComponentContext componentContext) {
        logger.info("Deactivating Application {}", this.getClass().getName());
        this.componentContext = null;
    }

    protected void setComponentContext(ComponentContext componentContext) {
        this.componentContext = componentContext;
    }

    /**
     * Some bundles set the componentContext, others (via blueprint) only the bundleContext... need to revisit
     * 
     * @return
     */
    public Bundle getBundle() {
        if (this.bundleContext != null) {
            return this.bundleContext.getBundle();
        }
        if (componentContext == null) {
            return null;
        }
        return componentContext.getBundleContext().getBundle();
    }

    public synchronized void setBundleContext(BundleContext bc) {
        this.bundleContext = bc;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public EventAdmin getEventAdmin() {
        return eventAdmin;
    }

    public void setAuthenticationService(AuthenticationService authService) {
        this.authenticationService = authService;
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public HtmlPolicyBuilder getNoHtmlPolicyBuilder() {
        return noHtmlPolicyBuilder;
    }

}
