package de.twenty11.skysail.server.security.shiro.subject.support;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.subject.WebSubject;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.security.shiro.subject.RestSubjectContext;
import de.twenty11.skysail.server.security.shiro.subject.RestletSubject;

public class SkysailWebSubjectContext extends DefaultSubjectContext implements RestSubjectContext {

    private static final long serialVersionUID = 4568742724548217247L;

    private static final String RESTLET_REQUEST = SkysailWebSubjectContext.class.getName() + ".RESTLET_REQUEST";
    private static final String RESTLET_RESPONSE = SkysailWebSubjectContext.class.getName() + ".RESTLET_RESPONSE";

    public SkysailWebSubjectContext() {
    }
    
    public SkysailWebSubjectContext(RestSubjectContext context) {
        super(context);
    }

    @Override
    public String resolveHost() {
        String host = super.resolveHost();
        if (host == null) {
            Request request = resolveRequest();
            if (request != null) {
                host = request.getHostRef().toString();
            }
        }
        return host;
    }

    @Override
    public Request getRequest() {
        return getTypedValue(RESTLET_REQUEST, Request.class);
    }

    @Override
    public Response getResponse() {
        return getTypedValue(RESTLET_RESPONSE, Response.class);
    }

    @Override
    public Request resolveRequest() {
        Request request = getRequest();

        //fall back on existing subject instance if it exists:
        if (request == null) {
            Subject existing = getSubject();
            if (existing instanceof RestletSubject) {
                request = ((RestletSubject) existing).getRequest();
            }
        }

        return request;
    }

    @Override
    public Response resolveResponse() {
        Response response = getResponse();

        //fall back on existing subject instance if it exists:
        if (response == null) {
            Subject existing = getSubject();
            if (existing instanceof RestletSubject) {
                response = ((RestletSubject) existing).getResponse();
            }
        }

        return response;
    }

    @Override
    public void setRequest(Request request) {
        if (request != null) {
            put(RESTLET_REQUEST, request);
        }
    }

    @Override
    public void setResponse(Response response) {
        if (response != null) {
            put(RESTLET_RESPONSE, response);
        }
    }

}
