package de.twenty11.skysail.server.restlet.filter;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Cookie;
import org.restlet.data.Form;
import org.restlet.routing.Filter;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * traces information when the request contains the debug=true parameter.
 * 
 * @ThreadSafe
 * 
 */
public class Tracer extends Filter {

    public static final String EVENT_PROPERTY_METHOD = "method";

    public static final String EVENT_PROPERTY_PATH = "path";

    private static final Logger logger = LoggerFactory.getLogger(Tracer.class);

    private AtomicBoolean doTrace = new AtomicBoolean(false);

    private EventAdmin eventAdmin;

    public Tracer(Context context, EventAdmin eventAdmin) {
        super(context);
        this.eventAdmin = eventAdmin;
    }

    @Override
    protected int beforeHandle(Request request, Response response) {
        fireEvent(request);
        checkRequest(request);
        traceRequest(request);
        return CONTINUE;
    }

    @Override
    protected void afterHandle(Request request, Response response) {
        trackResponse(response);
    }

    private void checkRequest(Request request) {
        doTrace.set(false);
        Form queryAsForm = request.getOriginalRef().getQueryAsForm();
        if (queryAsForm == null) {
            return;
        }
        String debugFlag = queryAsForm.getFirstValue("debug");
        if (!"true".equals(debugFlag)) {
            return;
        }
        doTrace.set(true);
    }

    private void traceRequest(Request request) {
        if (!doTrace.get()) {
            return;
        }
        logger.info("");
        logger.info("=== debug: request ========================");
        logger.info("{} '{}':", request.getMethod(), request.getResourceRef());

        Series<Cookie> cookies = request.getCookies();
        if (cookies != null && cookies.size() > 0) {
            logger.info(" > Cookies:");
            for (Cookie cookie : cookies) {
                logger.info("   {}: {}", cookie.getName(), cookie.getValue());
            }
        }
        logger.info("");
    }

    @SuppressWarnings("unchecked")
    private void fireEvent(Request request) {
        if (eventAdmin == null) {
            logger.warn("eventAdmin is null, cannot fire Event in {}", this.getClass().getName());
            return;
        }
        String origRequestPath = request.getOriginalRef().getPath();
        String topic = ("request/" + origRequestPath).replace("//", "/") + "ISSUED";
        @SuppressWarnings("rawtypes")
        Dictionary properties = new Hashtable();
        properties.put(EVENT_PROPERTY_PATH, origRequestPath);
        properties.put(EVENT_PROPERTY_METHOD, request.getMethod().toString());
        postEvent(topic, properties);
    }

    private void postEvent(String topic, Dictionary properties) {
        Event newEvent = new Event(topic, properties);
        try {
            eventAdmin.postEvent(newEvent);
        } catch (Exception e) {
            logger.warn("Exception caught when trying to post event", e);
        }
    }

    private void trackResponse(Response response) {
        if (!doTrace.get()) {
            return;
        }
        logger.info("");
        logger.info("=== debug: response ========================");
        logger.info("Status: {}, Age {}", response.getStatus(), response.getAge());
        logger.info("Allowed methods: {}", response.getAllowedMethods());
        logger.info("Auth Info:       {}", response.getAuthenticationInfo());
        logger.info("Attributes:      {}", response.getAttributes());
        logger.info("Challange Req:   {}", response.getChallengeRequests());
        logger.info("Dimensions:      {}", response.getDimensions());
        logger.info("Location Ref:    {}", response.getLocationRef());
        logger.info("Retry After:     {}", response.getRetryAfter());
        logger.info("Warnings:        {}", response.getWarnings());
        logger.info("");
    }

}
