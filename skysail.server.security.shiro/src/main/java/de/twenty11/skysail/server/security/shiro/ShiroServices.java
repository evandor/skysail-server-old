package de.twenty11.skysail.server.security.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.ClientInfo;
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

public class ShiroServices implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(ShiroServices.class);

    private Verifier verifier;
    private ServerConfiguration serverConfig;
    private BundleContext bundleContext;
    private DataSource dataSource;
    private final List<DataSourceFactory> dataSourceFactories = new ArrayList<DataSourceFactory>();

    public void initUrls() {
        // Ini ini = new Ini();
        // Section users = ini.addSection("users");
        // users.put("admin", "secret");
        //
        // Section roles = ini.addSection("roles");
        // roles.put("admin", "*");
        //
        // Factory<SecurityManager> factory = new IniSecurityManagerFactory(ini);
        // SecurityManager securityManager = factory.getInstance();
        //
        // verifier = new Verifier() {
        //
        // @Override
        // public int verify(Request request, Response response) {
        // return Verifier.RESULT_VALID;
        // }
        // };

        final Ini ini = new Ini();
        ini.addSection("main");

        // ini.getSection("main").put("authcRealm", "mySecurity.WebRealm");
        // ini.getSection("main").put("authc2", "org.apache.shiro.web.filter.authc.FormAuthenticationFilter");
        // ini.getSection("main").put("lookedUpRealm", "mySecurity.WebRealm");
        // ini.getSection("main").put("authc.loginUrl", "/login.jsp");
        // ini.getSection("main").put("authc2.loginUrl", "/login.jsp");
        // ini.getSection("main").put("securityManager.realms", "$lookedUpRealm");

        ini.addSection("urls");

        ini.getSection("urls").put("/secure/**", "authc");
        ini.getSection("urls").put("/login.jsp", "authc");
        ini.getSection("urls").put("/", "authc2");

        // try {
        // filter.init(filterConfig);
        // return filter;
        // } catch (ServletException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // return null;
        // }
        // Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(ini);
        // org.apache.shiro.mgt.SecurityManager sm = new DefaultWebSecurityManager();
    }

    public void init() {
        SkysailAuthorizingRealm skysailRealm = new SkysailAuthorizingRealm();
        if (dataSource != null) {
            return;
        }
        dataSource = getDataSource();
        if (dataSource == null) {
            return;
        }
        skysailRealm.setDataSource(dataSource);
        SecurityManager securityManager = new DefaultSecurityManager(skysailRealm);
        SecurityUtils.setSecurityManager(securityManager);

        // //
        // http://mail-archives.apache.org/mod_mbox/shiro-user/201201.mbox/%3CCAETPiXYQLUrjjG2K4qDuesSeq7+LBR1W1u4DUyU5WsmqNGSivA@mail.gmail.com%3E
        // FilterChainManager fcm = new DefaultFilterChainManager();
        // fcm.addFilter("urlFilter", initUrls());
        //
        // PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
        // filterChainResolver.setFilterChainManager(fcm);
        //
        // ShiroFilter filter = new ShiroFilter();
        // filter.setFilterChainResolver(filterChainResolver);

        // ((WebSecurityManager)securityManager).

        verifier = new Verifier() {

            @Override
            public int verify(Request request, Response response) {
                Subject currentUser = SecurityUtils.getSubject();
                String path = request.getOriginalRef().getPath();
                if ("/".equals(path) || "/login".equals(path)) {
                    return Verifier.RESULT_VALID;
                }

                if (currentUser.isAuthenticated()) {
                    return Verifier.RESULT_VALID;
                }
                String username = request.getChallengeResponse() == null ? null : request.getChallengeResponse()
                        .getIdentifier();
                char[] secret = request.getChallengeResponse() == null ? null : request.getChallengeResponse()
                        .getSecret();
                UsernamePasswordToken token = new UsernamePasswordToken(username, secret);
                try {
                    currentUser.login(token);
                } catch (Exception e) {
                    return Verifier.RESULT_INVALID;
                }
                return Verifier.RESULT_VALID;
            }
        };

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
    public void login(String username, String password) {
        Subject currentUser = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        currentUser.login(token);
    }

    @Override
    public void logout() {
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();
    }

    @Override
    public Authenticator getAuthenticator(Context context) {
        ChallengeAuthenticator guard = new ChallengeAuthenticator(context, ChallengeScheme.CUSTOM, "realm");
        guard.setVerifier(this.verifier);
        guard.setEnroler(new Enroler() {
            @Override
            public void enrole(ClientInfo clientInfo) {
                List<Role> defaultRoles = new ArrayList<Role>();
                Subject currentUser = SecurityUtils.getSubject();
                if (currentUser.hasRole("administrator")) {
                    Role userRole = new Role("admin", "standard role");
                    defaultRoles.add(userRole);
                }
                clientInfo.setRoles(defaultRoles);
            }
        });
        return guard;
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
}
