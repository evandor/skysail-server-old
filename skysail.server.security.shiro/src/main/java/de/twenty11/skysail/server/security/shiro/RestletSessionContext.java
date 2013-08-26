package de.twenty11.skysail.server.security.shiro;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

public interface RestletSessionContext extends SessionContext, RestletRequestPairSource {

    Request getRequest();

    void setRequest(Request request);

    Response getResponse();

    void setResponse(Response response);
}
