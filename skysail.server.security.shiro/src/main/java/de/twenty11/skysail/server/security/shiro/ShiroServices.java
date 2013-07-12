package de.twenty11.skysail.server.security.shiro;

import de.twenty11.skysail.server.security.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
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

public class ShiroServices implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(ShiroServices.class);
    private Verifier verifier;

    public ShiroServices() {
        // IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // SecurityManager securityManager = factory.getInstance();
        // SecurityManager securityManager = new DefaultSecurityManager(realm);

        Ini ini = new Ini();
        Section users = ini.addSection("users");
        users.put("admin", "secret");

        Section roles = ini.addSection("roles");
        roles.put("admin", "*");

        Factory<SecurityManager> factory = new IniSecurityManagerFactory(ini);
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        verifier = new Verifier() {

            @Override
            public int verify(Request request, Response response) {
                Subject currentUser = SecurityUtils.getSubject();
                if (currentUser.isAuthenticated()) {
                    return Verifier.RESULT_VALID;
                }
                String username = request.getChallengeResponse() == null ? null : request.getChallengeResponse().getIdentifier();
                char[] secret = request.getChallengeResponse() == null ? null : request.getChallengeResponse().getSecret();
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
        ChallengeAuthenticator guard = new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "realm");
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
        return guard;
    }
}
