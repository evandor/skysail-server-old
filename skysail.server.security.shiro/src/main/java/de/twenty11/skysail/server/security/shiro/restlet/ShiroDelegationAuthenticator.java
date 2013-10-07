package de.twenty11.skysail.server.security.shiro.restlet;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.routing.Filter;

public class ShiroDelegationAuthenticator extends CookieAuthenticator {

    public ShiroDelegationAuthenticator(Context context, String realm, byte[] encryptSecretKey) {
        super(context, realm, encryptSecretKey);
        setIdentifierFormName("username");
        setSecretFormName("password");
        setLoginFormPath("/login");
        setOptional(true); // we want anonymous users too
        setVerifier(new ShiroDelegatingVerifier());
    }

    @Override
    protected int logout(Request request, Response response) {
        int result = super.logout(request, response);
        if (Filter.STOP == result) {
            Subject subject = SecurityUtils.getSubject();
            subject.logout();
        }
        return result;
    }
}
