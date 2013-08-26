package de.twenty11.skysail.server.security.shiro.subject;

import org.apache.shiro.subject.SubjectContext;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.security.shiro.util.RestletRequestPairSource;

public interface RestSubjectContext extends SubjectContext, RestletRequestPairSource {

    Request resolveRequest();

    Response resolveResponse();

    void setRequest(Request request);

    void setResponse(Response response);
}
