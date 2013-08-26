package de.twenty11.skysail.server.security.shiro;

import java.util.concurrent.Callable;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.WebSubject;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.routing.Filter;

import de.twenty11.skysail.server.security.shiro.subject.RestletSubject;

public class ShiroDelegationFilter extends Filter {

    public ShiroDelegationFilter(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected int doHandle(final Request request, final Response response) {
        final int result = CONTINUE;
        
        final Subject subject = createSubject(request, response);
        
        subject.execute(new Callable() {
            public Object call() throws Exception {
                //updateSessionLastAccessTime(request, response);
                //executeChain(request, response, chain);
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
                
                return null;
            }
        });

//        if (getNext() != null) {
//            getNext().handle(request, response);
//
//            // Re-associate the response to the current thread
//            Response.setCurrent(response);
//
//            // Associate the context to the current thread
//            if (getContext() != null) {
//                Context.setCurrent(getContext());
//            }
//        } else {
//            response.setStatus(Status.SERVER_ERROR_INTERNAL);
//            getLogger()
//                    .warning(
//                            "The filter "
//                                    + getName()
//                                    + " was executed without a next Restlet attached to it.");
//        }

        return result;
    }

    private Subject createSubject(Request request, Response response) {
        return new RestletSubject.Builder(request, response).buildWebSubject();
    }

}
