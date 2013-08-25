package de.twenty11.skysail.server.security.shiro;

import org.apache.shiro.subject.SubjectContext;
import org.restlet.Request;
import org.restlet.Response;

public interface RestSubjectContext extends SubjectContext, RestletRequestPairSource {

    Request resolveRequest();

    Response resolveResponse();

    void setRequest(Request request);

    void setResponse(Response response);
}
