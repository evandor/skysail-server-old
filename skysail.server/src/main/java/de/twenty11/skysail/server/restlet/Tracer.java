package de.twenty11.skysail.server.restlet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.Cookie;
import org.restlet.data.MediaType;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

public class Tracer extends Filter {

    public Tracer(Context context) {
        super(context);
    }

    @Override
    protected int doHandle(Request request, Response response) {
        //String debugFlag = (String) request.getEntity().get
        //if (debugFlag != null) {
            StringBuffer sb = new StringBuffer("=== debug ========================\n");
            sb.append(request.getMethod()).append(" ").append(request.getResourceRef()).append(" >>>\n");
            Series<Cookie> cookies = request.getCookies();
            if (cookies != null && cookies.size() > 0) {
                sb.append("Cookies:\n");
                for (Cookie cookie : cookies) {
                    sb.append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
                }
            }
            System.out.println(sb.toString());
        //}

        return CONTINUE;
    }

}
