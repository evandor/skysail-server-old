package de.skysail.server.communication.osgi;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import de.skysail.server.security.UserPrincipal;

/**
 * The skysailOsgiDispatcherServlet is not meant to be called directly - the only
 * way to call it is via the skysail Server Component. 
 * 
 * The idea is that the original request is dealt by skysail server using a restlet approach. 
 * If skysail server needs to interact with the OSGi layer, this is done by wrapping the 
 * original request and slighly alter it - adding a prefix. Now the OSGi layer can take care
 * (via the servlet bridge), handle the request (like a servlet), but pass back the needed
 * information via the response.
 * 
 * Typically, a request from the outside trying to access the altered path should be restricted
 * by the security layer.
 * 
 * @author Graef
 *
 */
public class SkysailOsgiRequestWrapper extends HttpServletRequestWrapper {

    /**
     * this prefix will be added to the return pathInfo.
     */
    private String pathPrefix;
    
    private UserPrincipal principal;

    /**
     * the constructor takes the original request and the additional prefix.
     * @param username 
     * 
     * @param request the original request
     * @param prefix the prefix to be returned by pathInfo
     */
    public SkysailOsgiRequestWrapper(String username, final HttpServletRequest request, final String prefix) {
        super(request);
        this.pathPrefix = prefix;
        this.principal = new UserPrincipal(username);
    }
        
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequestWrapper#getPathInfo()
     */
    @Override
    public final String getPathInfo() {
        return pathPrefix + super.getPathInfo();
    }
    
    @Override
    public Principal getUserPrincipal() {
        //Principal dummy = super.getUserPrincipal();
        return this.principal;
    }

}
