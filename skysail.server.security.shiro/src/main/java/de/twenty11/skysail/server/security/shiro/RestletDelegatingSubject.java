package de.twenty11.skysail.server.security.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.restlet.Request;
import org.restlet.Response;

public class RestletDelegatingSubject extends DelegatingSubject implements RestletSubject {

    private Request request;
    private Response response;

    public RestletDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host,
            Session session, boolean sessionEnabled, Request request, Response response, SecurityManager securityManager) {
        super(principals, authenticated, host, session, sessionEnabled,securityManager);
        this.request = request;
        this.response = response;
    }

    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response getResponse() {
        return response;
    }

}
