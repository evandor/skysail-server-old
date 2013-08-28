package de.twenty11.skysail.server.security.shiro.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.crypto.CookieAuthenticator;
import org.restlet.security.SecretVerifier;
import org.restlet.security.Verifier;

public class ShiroDelegationAuthenticator extends CookieAuthenticator {

    public ShiroDelegationAuthenticator(Context context, String realm, byte[] encryptSecretKey) {
        super(context, realm, encryptSecretKey);
        setIdentifierFormName("username");
        setIdentifierFormName("password");
        setLoginFormPath("/login");
        setVerifier(new SecretVerifier() {
            
            @Override
            public int verify(String identifier, char[] secret) {
                // TODO Auto-generated method stub
                return RESULT_VALID; //CONTINUE;
            }
        });
    }


}
