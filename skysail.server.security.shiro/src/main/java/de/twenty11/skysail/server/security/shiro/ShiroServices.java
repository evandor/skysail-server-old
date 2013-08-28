package de.twenty11.skysail.server.security.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.ClientInfo;
import org.restlet.routing.Filter;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Enroler;
import org.restlet.security.Role;
import org.restlet.security.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.Constants;
import de.twenty11.skysail.server.config.ServerConfiguration;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.security.shiro.mgt.SkysailWebSecurityManager;
import de.twenty11.skysail.server.security.shiro.restlet.ShiroDelegationAuthenticator;
import de.twenty11.skysail.server.security.shiro.restlet.ShiroDelegationFilter;

/**
 * Default AuthenticationService Implementation shipped with skysail
 *
 */
public class ShiroServices implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(ShiroServices.class);

    private Verifier verifier;
    private ServerConfiguration serverConfig;
    private BundleContext bundleContext;
    private DataSource dataSource;
    private final List<DataSourceFactory> dataSourceFactories = new ArrayList<DataSourceFactory>();

    private SkysailAuthorizingRealm skysailRealm;

    public void init() {
        logger.info("initializing {}", this.getClass().getSimpleName());
        skysailRealm = new SkysailAuthorizingRealm();
        if (dataSource != null) {
            return;
        }
        dataSource = getDataSource();
        if (dataSource == null) {
            return;
        }
        
        logger.info("Setting datasource for SkysailAuthorizingRealm: {}", dataSource.toString());
        skysailRealm.setDataSource(dataSource);
        
        logger.info("Creating new SkysailWebSecurityManager...");
        SkysailWebSecurityManager securityManager = new SkysailWebSecurityManager(skysailRealm);

        logger.info("Setting new SkysailWebSecurityManager as Shiros SecurityManager");
        SecurityUtils.setSecurityManager(securityManager);

        verifier = new Verifier() {

            @Override
            public int verify(Request request, Response response) {
                return Verifier.RESULT_VALID;
            }
        };
    }
    
    @Override
    public void login(String username, String password,Request request, Response response) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        logger.info("login event for user '{}'", username);
        currentUser.login(token);
        logger.info("login for {} yield to {}", username, SecurityUtils.getSubject());
    }

    @Override
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        logger.info("logout event for user '{}'", currentUser.getPrincipal());
        currentUser.logout();
        logger.info("logout yield to {}", currentUser);
    }



    private DataSource getDataSource() {
        if (serverConfig == null) {
            return null;
        }
        String driver = serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_DRIVER);

        Properties props = new Properties();
        props.put(DataSourceFactory.JDBC_PASSWORD, serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_PASSWORD));
        props.put(DataSourceFactory.JDBC_URL, serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_URL));
        props.put(DataSourceFactory.JDBC_USER, serverConfig.getConfigForKey(Constants.SKYSAIL_JDBC_USER));

        ServiceReference<?>[] allDatasourceFactoryServiceReferences;
        try {
            allDatasourceFactoryServiceReferences = bundleContext.getAllServiceReferences(
                    "org.osgi.service.jdbc.DataSourceFactory", null);
            if (allDatasourceFactoryServiceReferences == null) {
                return null;
            }
            for (ServiceReference<?> sr : allDatasourceFactoryServiceReferences) {
                String driverProperty = (String) sr.getProperty("osgi.jdbc.driver.class");
                if (driver.equals(driverProperty)) {
                    DataSourceFactory dsf = (DataSourceFactory) bundleContext.getService(sr);
                    return dsf.createDataSource(props);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Authenticator getAuthenticator(Context context) {
        return new ShiroDelegationAuthenticator(context, "MyRealm", "secret".getBytes());
//        ChallengeAuthenticator guard = new ChallengeAuthenticator(context, ChallengeScheme.CUSTOM, "realm");
//        guard.setVerifier(this.verifier);
//        guard.setEnroler(new Enroler() {
//            @Override
//            public void enrole(ClientInfo clientInfo) {
//                List<Role> defaultRoles = new ArrayList<Role>();
//                Subject currentUser = SecurityUtils.getSubject();
//                if (currentUser.hasRole("administrator")) {
//                    Role userRole = new Role("admin", "standard role");
//                    defaultRoles.add(userRole);
//                }
//                clientInfo.setRoles(defaultRoles);
//            }
//        });
//        return guard;
    }

    public void setServerConfig(ServerConfiguration serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void setBundleContext(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    public void registerDSF(DataSourceFactory dsf) {
        this.dataSourceFactories.add(dsf);
        if (dataSource != null) {
            return;
        }
        init();
    }

    public void unregisterDSF(DataSourceFactory dsf) {

    }

    @Override
    public Filter getRestletShiroFilter(Context context) {
        return new ShiroDelegationFilter(context);
    }

}
