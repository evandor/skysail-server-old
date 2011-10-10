package de.skysail.server.communication.osgi;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * The skysailOsgiDispatcherServlet is not meant to be called directly - the only
 * way to call it is via the skysail Server Component. 
 * 
 * The idea is that the original request is dealt by skysail server using a restlet approach. 
 * If skysail server needs to interact with the OSGi layer, this is done by wrapping the 
 * original request and slighly alter it - adding a prefix. Now the OSGi layer can take care
 * (via the servlet bridge), handle the request (like a servlet), but pass back the needed
 * information via the response, which is wrapped by this object.
 * 
 * Typically, a request from the outside trying to access the altered path should be restricted
 * by the security layer.
 * 
 * @author Graef
 *
 */
public class SkysailOsgiResponseWrapper extends HttpServletResponseWrapper {

    /**
     * the constructor takes the original request and the additional prefix.
     * 
     * @param response the original response
     */
    public SkysailOsgiResponseWrapper(final HttpServletResponse response) {
        super(response);
    }
    

}
