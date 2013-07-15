package de.twenty11.skysail.server.security.shiro;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.realm.jdbc.JdbcRealm;

public class SkysailAuthorizingRealm extends JdbcRealm {

    public SkysailAuthorizingRealm() {
        setAuthenticationQuery("select password from um_users where username = ?");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return super.doGetAuthenticationInfo(token);
    }

    @Override
    protected Set<String> getRoleNamesForUser(Connection conn, String username) throws SQLException {
        // TODO Auto-generated method stub
        return super.getRoleNamesForUser(conn, username);
    }

    @Override
    protected Set<String> getPermissions(Connection conn, String username, Collection<String> roleNames)
            throws SQLException {
        // TODO Auto-generated method stub
        return super.getPermissions(conn, username, roleNames);
    }

}
