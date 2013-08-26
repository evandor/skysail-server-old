package de.twenty11.skysail.server.security.shiro;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

public class RestletUtils {

    public static Request getRequest(Object requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getRequest();
        }
        return null;
    }

    public static Response getResponse(SessionContext requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getResponse();
        }
        return null;
    }


}
