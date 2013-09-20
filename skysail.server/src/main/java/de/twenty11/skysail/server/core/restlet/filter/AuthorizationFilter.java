//package de.twenty11.skysail.server.core.restlet.filter;
//
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
//import org.restlet.Request;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//import de.twenty11.skysail.server.core.restlet.SkysailServerResource;
//
//public class AuthorizationFilter<R extends SkysailServerResource<T>, T> extends AbstractResourceFilter<R, T> {
//
//    private static Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);
//
//    @Override
//    protected FilterResult beforeHandle(R resource, Request request, ResponseWrapper<T> response) {
//        logger.debug("entering {}#beforeHandle", this.getClass().getSimpleName());
//
//        Subject subject = SecurityUtils.getSubject();
//        if (!subject.hasAllRoles(roleIdentifiers)) {
//            return FilterResult.STOP;
//        }
//        // String[] permissions;
//        // boolean[] permitted = subject.isPermitted(permissions);
//        // for (boolean permission : permitted) {
//        // if (!permission) {
//        // return FilterResult.STOP;
//        // }
//        // }
//
//        return FilterResult.CONTINUE;
//    }
//
// }
