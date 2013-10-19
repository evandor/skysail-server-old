package de.twenty11.skysail.server.core.restlet.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.UserNotAuthenticatedResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailServerResource;

public class CheckUserIsLoggedInFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(CheckUserIsLoggedInFilter.class);

    @Override
    protected FilterResult beforeHandle(R resource, Request request, ResponseWrapper<T> response) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return FilterResult.CONTINUE;
        }
        logger.debug("creating new UserNotAuthenticatedResponse");
        response.setSkysailResponse(new UserNotAuthenticatedResponse<T>());
        return FilterResult.STOP;
    }

}
