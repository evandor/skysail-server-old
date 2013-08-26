package de.twenty11.skysail.server.security.shiro;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.CookieSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkysailWebSessionManager extends DefaultSessionManager implements WebSessionManager {

    private static Logger logger = LoggerFactory.getLogger(SkysailWebSessionManager.class);

    //private CookieSetting sessionIdCookie;
    private boolean sessionIdCookieEnabled;

    public SkysailWebSessionManager() {
        this.sessionIdCookieEnabled = true;
        //this.sessionIdCookie = cookieSetting;
        //Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
        //cookie.setHttpOnly(true); //more secure, protects against XSS attacks
    }
    
    public boolean isSessionIdCookieEnabled() {
        return sessionIdCookieEnabled;
    }

    public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
        this.sessionIdCookieEnabled = sessionIdCookieEnabled;
    }

    /**
     * Stores the Session's ID, usually as a Cookie, to associate with future requests.
     * 
     * @param session
     *            the session that was just {@link #createSession created}.
     */
    @Override
    protected void onStart(Session session, SessionContext context) {
        // super.onStart(session, context);

        // if (!WebUtils.isHttp(context)) {
        // log.debug("SessionContext argument is not HTTP compatible or does not have an HTTP request/response " +
        // "pair. No session ID cookie will be set.");
        // return;
        //
        // }
        Request request = RestletUtils.getRequest(context);
        Response response = RestletUtils.getResponse(context);

        if (isSessionIdCookieEnabled()) {
            Serializable sessionId = session.getId();
            storeSessionId(sessionId, request, response);
        } else {
            // log.debug("Session ID cookie is disabled.  No cookie has been set for new session with id {}",
            // session.getId());
        }

        //request.removeAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE);
        //request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_IS_NEW, Boolean.TRUE);
    }

    @Override
    protected void onStop(Session session, SessionKey key) {
        super.onStop(session, key);
        if (WebUtils.isHttp(key)) {
            HttpServletRequest request = WebUtils.getHttpRequest(key);
            HttpServletResponse response = WebUtils.getHttpResponse(key);
            // log.debug("Session has been stopped (subject logout or explicit stop).  Removing session ID cookie.");
            //removeSessionIdCookie(request, response);
        } else {
            // log.debug("SessionKey argument is not HTTP compatible or does not have an HTTP request/response " +
            // "pair. Session ID cookie will not be removed due to stopped session.");
        }
    }

    private void storeSessionId(Serializable currentId, Request request, Response response) {
        if (currentId == null) {
            String msg = "sessionId cannot be null when persisting for subsequent requests.";
            throw new IllegalArgumentException(msg);
        }
        
        CookieSetting cookie = createCookie();
        //Cookie template = getSessionIdCookie();
        //Cookie cookie = new SimpleRestletCookie(template);
        String idString = currentId.toString();
        cookie.setValue(idString);
        //cookie.saveTo(request, response);

        response.getCookieSettings().add(cookie);

        // log.trace("Set session ID cookie for session with id {}", idString);
    }

    private CookieSetting createCookie() {
        CookieSetting cookieSetting = new CookieSetting(ShiroHttpSession.DEFAULT_SESSION_ID_NAME,null);
        cookieSetting.setAccessRestricted(true);
        cookieSetting.setPath("/");
        cookieSetting.setComment("Skysail cookie-based authentication");
        cookieSetting.setMaxAge(30);
        return cookieSetting;
    }

    private void removeSessionIdCookie(Request request, Response response) {
        //getSessionIdCookie().removeFrom(request, response);
    }

    @Override
    public boolean isServletContainerSessions() {
        return false;
    }

}
