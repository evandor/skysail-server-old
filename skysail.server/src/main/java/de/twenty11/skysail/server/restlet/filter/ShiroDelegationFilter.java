package de.twenty11.skysail.server.restlet.filter;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.servlet.ShiroFilter;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.ext.servlet.ServletUtils;
import org.restlet.routing.Filter;

public class ShiroDelegationFilter extends Filter {

    private class SkysailShiroFilter extends ShiroFilter {
        @Override
        public void init() throws Exception {
            super.init();
        }
    }
    
    private SkysailShiroFilter shiroFilter;

    public ShiroDelegationFilter(Context context) {
        super(context);
//        shiroFilter = new SkysailShiroFilter();
//        try {
//            shiroFilter.init();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    @Override
    protected int doHandle(Request request, Response response) {
        final int result = CONTINUE;

//        EnvironmentLoader loader = new EnvironmentLoader();
//        ServletContext sc = (ServletContext)getContext().getServerDispatcher().getContext();
//        loader.initEnvironment(sc);

//        try {
//            shiroFilter.doFilter(ServletUtils.getRequest(request), ServletUtils.getResponse(response), null);
//        } catch (ServletException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        if (getNext() != null) {
            getNext().handle(request, response);

            // Re-associate the response to the current thread
            Response.setCurrent(response);

            // Associate the context to the current thread
            if (getContext() != null) {
                Context.setCurrent(getContext());
            }
        } else {
            response.setStatus(Status.SERVER_ERROR_INTERNAL);
            getLogger().warning("The filter " + getName() + " was executed without a next Restlet attached to it.");
        }

        return result;
    }

}
