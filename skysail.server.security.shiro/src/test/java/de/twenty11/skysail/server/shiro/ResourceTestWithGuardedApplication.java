package de.twenty11.skysail.server.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import de.twenty11.skysail.server.ResourceTestWithApplication;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;
import de.twenty11.skysail.server.security.AuthorizationService;
import de.twenty11.skysail.server.security.shiro.ShiroServices;

public class ResourceTestWithGuardedApplication<T extends SkysailApplication> extends ResourceTestWithApplication {

    private ShiroServices authService;

    public ResourceTestWithGuardedApplication() {
        AuthorizingRealm skysailRealm = new AuthorizingRealm() {

            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
                return null;
            }

            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
                    throws AuthenticationException {
                SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(token.getPrincipal(),
                        token.getCredentials(), "testrealm");
                return authenticationInfo;
            }

        };
        // if (dataSource == null) {
        // dataSource = getDataSourceFromConfig();
        // }
        // if (dataSource == null) {
        // return;
        // }

        // skysailRealm.setDataSource(dataSource);
        authService = new ShiroServices(skysailRealm);
        // authService.setDataSource(getTestDbDataSource());
        // authService.init();
    }

    @Override
    protected AuthenticationService getAuthenticationService() {
        return authService;
    }

    @Override
    protected AuthorizationService getAuthorizationService() {
        // TODO Auto-generated method stub
        return null;
    }
}
