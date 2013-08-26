package de.twenty11.skysail.server.security.shiro.util;

import org.restlet.Request;
import org.restlet.Response;

public class RestletUtils {

    public static Request getRequest(Object requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getRequest();
        }
        return null;
    }

    public static Response getResponse(Object requestPairSource) {
        if (requestPairSource instanceof RestletRequestPairSource) {
            return ((RestletRequestPairSource) requestPairSource).getResponse();
        }
        return null;
    }


}
