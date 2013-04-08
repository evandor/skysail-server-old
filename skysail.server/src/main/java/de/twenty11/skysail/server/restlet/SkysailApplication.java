package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.ClientInfo;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.resource.ServerResource;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Enroler;
import org.restlet.security.MapVerifier;
import org.restlet.security.Role;
import org.restlet.security.Verifier;
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.converter.IFrame2BootstrapConverter;
import de.twenty11.skysail.server.converter.Json2BootstrapConverter;
import de.twenty11.skysail.server.converter.Json2HtmlConverter;
import de.twenty11.skysail.server.converter.ToCsvConverter;
import de.twenty11.skysail.server.converter.ToPdfConverter;
import de.twenty11.skysail.server.internal.Blocker;
import de.twenty11.skysail.server.listener.UrlMappingServiceListener;

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

    /** slf4j based logger implementation. */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** the restlet router. */
    protected volatile SkysailRouter router;

    private Verifier verifier = new MapVerifier();

    /** the osgi bundle context. */
    private BundleContext bundleContext;

    /** listener keeping track of all url mappings. */
    protected UrlMappingServiceListener urlMappingServiceListener;

    public SkysailApplication(Context context) {
        //ConfigService configService = null;// ConfigServiceProvider.getConfigService();
        List<ConverterHelper> registeredConverters = Engine.getInstance().getRegisteredConverters();
        registeredConverters.add(new Json2HtmlConverter());
        registeredConverters.add(new Json2BootstrapConverter());
        registeredConverters.add(new IFrame2BootstrapConverter());
        registeredConverters.add(new ToCsvConverter());
        registeredConverters.add(new ToPdfConverter());
        setContext(context);
    }

    abstract protected void attach();

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public BundleContext getBundleContext() {
        return bundleContext;
    }

    @Override
    public Restlet createInboundRoot() {

        logger.info("creating new Router in {}", this.getClass().getName());
        router = new SkysailRouter(getContext());
        // router.setDefaultMatchingQuery(true);

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        // getConnectorService().getClientProtocols().add(Protocol.HTTPS);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);

        attach();

        Timer timer = new Timer(getContext());
        Blocker blocker = new Blocker(getContext());
        Tracer tracer = new Tracer(getContext());
        OriginalRequestFilter originalRequestFilter = new OriginalRequestFilter(getContext());
        blocker.setNext(originalRequestFilter);
        originalRequestFilter.setNext(router);

        ChallengeAuthenticator guard = new ChallengeAuthenticator(getContext(), ChallengeScheme.HTTP_BASIC, "realm");
        guard.setVerifier(this.verifier);
        guard.setEnroler(new Enroler() {

            @Override
            public void enrole(ClientInfo clientInfo) {
                List<Role> defaultRoles = new ArrayList<Role>();
                Role userRole = new Role("user", "standard role");
                defaultRoles.add(userRole);
                clientInfo.setRoles(defaultRoles);
            }
        });
        timer.setNext(blocker);
        guard.setNext(timer);

        return guard;
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

    public UrlMappingServiceListener getUrlMappingServiceListener() {
        return urlMappingServiceListener;
    }

    public void setVerifier(Verifier verifier) {
        this.verifier = verifier;
    }

}
