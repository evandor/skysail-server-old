package de.twenty11.skysail.server.security.shiro.restlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.security.SecretVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroDelegationAuthenticator extends CookieAuthenticator {

    private static final Logger logger = LoggerFactory.getLogger(ShiroDelegationAuthenticator.class);

    public ShiroDelegationAuthenticator(Context context, String realm, byte[] encryptSecretKey) {
        super(context, realm, encryptSecretKey);
        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath("/login");
        setVerifier(new SecretVerifier() {

            @Override
            public int verify(String identifier, char[] secret) {

                Subject currentUser = SecurityUtils.getSubject();
                UsernamePasswordToken token = new UsernamePasswordToken(identifier, secret.toString());
                logger.info("login event for user '{}'", identifier);
                try {
                    currentUser.login(token);
                } catch (UnknownAccountException uae) {
                    logger.info("UnknownAccountException '{}' when login in {}", uae.getMessage(), identifier);
                } catch (IncorrectCredentialsException ice) {
                    logger.info("IncorrectCredentialsException '{}' when login in {}", ice.getMessage(), identifier);
                } catch (LockedAccountException lae) {
                    logger.info("LockedAccountException '{}' when login in {}", lae.getMessage(), identifier);
                } catch (AuthenticationException ae) {
                    logger.error("AuthenticationException '{}' when login in {}", ae.getMessage(), identifier);
                }
                // valid, as user can logging as "anonymous"
                return RESULT_VALID;
            }
        });
    }

}
