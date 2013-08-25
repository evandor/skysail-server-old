package de.twenty11.skysail.server.security.shiro;

import java.util.Map;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.subject.WebSubject;
import org.restlet.Request;
import org.restlet.Response;

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
    public void putAll(Map<? extends String, ? extends Object> m) {
        // TODO Auto-generated method stub
        
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
            if (existing instanceof WebSubject) {
                return null;
                //request = ((WebSubject) existing).getServletRequest();
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
            if (existing instanceof WebSubject) {
                return null;
                //response = ((WebSubject) existing).getServletResponse();
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
