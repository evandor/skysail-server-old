package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.ClientInfo;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Enroler;
import org.restlet.security.MapVerifier;
import org.restlet.security.Role;
import org.restlet.security.SecretVerifier;
import org.restlet.util.RouteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.internal.Blocker;
import de.twenty11.skysail.server.internal.ClassLoaderDirectory;
import de.twenty11.skysail.server.internal.CompositeClassLoader;
import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.services.ConfigService;

/**
 * 
 * Concurrency note from parent class: instances of this class or its subclasses can be invoked by several threads at
 * the same time and therefore must be thread-safe. You should be especially careful when storing state in member
 * variables.
 * 
 * @author carsten
 * 
 */
public abstract class RestletOsgiApplication extends Application {

    /** slf4j based logger implementation. */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** the restlet router. */
    protected volatile Router router;

    private SecretVerifier verifier = new MapVerifier();

    private String staticPath;

    /** the 'name' of the application, e.g. "dbviewer" */
    private String applicationName;

    /** the osgi bundle context. */
    private static BundleContext bundleContext;
    
    /** listener keeping track of all url mappings. */
    protected UrlMappingServiceListener urlMappingServiceListener;

    public RestletOsgiApplication(String applicationName, String staticPathTemplate) {
        this.applicationName = applicationName;
        ConfigService configService = null;// ConfigServiceProvider.getConfigService();
        this.staticPath = staticPathTemplate;
    }

    abstract protected void attach();

    public static void setBundleContext(BundleContext bundleContext) {
        RestletOsgiApplication.bundleContext = bundleContext;
    }

    public String getApplicationName() {
        return applicationName;
    }
    
    @Override
    public final Restlet createInboundRoot() {

        logger.info("creating new Router in {}", this.getClass().getName());
        router = new Router(getContext());

        // see
        // http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
        getConnectorService().getClientProtocols().add(Protocol.HTTP);
        getConnectorService().getClientProtocols().add(Protocol.HTTPS);
        getConnectorService().getClientProtocols().add(Protocol.FILE);
        getConnectorService().getClientProtocols().add(Protocol.CLAP);

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/webapp/");
        CompositeClassLoader customCL = new CompositeClassLoader();
        // TODO check ordering
        // add "this" classloader first (this is usually the "product" bundle
        customCL.addClassLoader(this.getClass().getClassLoader());
        // this is the "restletosgi" bundle
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        // customCL.addClassLoader(Router.class.getClassLoader());

        ClassLoaderDirectory directory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        router.attach(this.staticPath, directory);

        attach();

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
        guard.setNext(blocker);

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

    public UrlMappingServiceListener getUrlMappingServiceListener() {
        return urlMappingServiceListener;
    }

    public void setVerifier(SecretVerifier verifier) {
        this.verifier = verifier;
    }


}
