package de.twenty11.skysail.server.security.shiro.util;

import org.restlet.Request;
import org.restlet.Response;

public interface RestletRequestPairSource {

    Request getRequest();

    Response getResponse();

}
