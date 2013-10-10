package de.twenty11.skysail.server.core.restlet;

import java.util.List;

import org.restlet.data.Method;

import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckBusinessViolationsFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputFilter;
import de.twenty11.skysail.server.core.restlet.filter.DataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter;
import de.twenty11.skysail.server.core.restlet.filter.FormDataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.PersistEntityFilter;
import de.twenty11.skysail.server.core.restlet.filter.QueryExtractingFilter;

public class RequestHandler<T> {

    /**
     * for now, always return new objects
     */
    public static synchronized <T> AbstractResourceFilter<ListServerResource<T>, List<T>> createForList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForListGet();
        } else if (method.equals(Method.POST)) {
            return chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    /**
     * for now, always return new objects
     */
    public static <T> AbstractResourceFilter<EntityServerResource<T>, T> createForEntity(Method method) {
        if (method.equals(Method.GET)) {
            return chainForEntityGet();
        } else if (method.equals(Method.POST)) {
            return chainForEntityPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    // @formatter:off

    private static <T> AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListPost() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>()
                //.calling(new AuthorizationFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckInvalidInputFilter<ListServerResource<T>, List<T>>())
                .calling(new FormDataExtractingFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckBusinessViolationsFilter<ListServerResource<T>, List<T>>())
                .calling(new PersistEntityFilter<ListServerResource<T>, List<T>>());
    }

    private static <T> AbstractResourceFilter<ListServerResource<T>, List<T>> chainForListGet() {
        return new ExceptionCatchingFilter<ListServerResource<T>, List<T>>()
                .calling(new DataExtractingFilter<ListServerResource<T>, List<T>>());
    }

    private static <T> AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityGet() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                .calling(new QueryExtractingFilter<EntityServerResource<T>, T>())
                .calling(new DataExtractingFilter<EntityServerResource<T>, T>());
    }

    private static <T> AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                //.calling(new AuthorizationFilter<ListServerResource<T>, List<T>>())
                .calling(new CheckInvalidInputFilter<EntityServerResource<T>, T>())
                .calling(new FormDataExtractingFilter<EntityServerResource<T>, T>())
                .calling(new CheckBusinessViolationsFilter<EntityServerResource<T>, T>())
                .calling(new PersistEntityFilter<EntityServerResource<T>, T>());
    }

    
    public AbstractResourceFilter<ListServerResource<T>, List<T>> getChain(Method method) {
        if (method.equals(Method.GET)) {
        
            return null;
        } else if (method.equals(Method.POST)) {

            // exceptionCatching -> QueryExtracting -> DataExtracting
            return  chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public AbstractResourceFilter<EntityServerResource<T>, T> getChainForEntity(Method method) {
        if (method.equals(Method.GET)) {
        
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                        .calling(new QueryExtractingFilter<EntityServerResource<T>, T>())
                        //.calling(new DataExtractingFilter<T>())
                    ;
        
        } else if (method.equals(Method.POST)) {

            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter<EntityServerResource<T>, T>()
                        .calling(new CheckInvalidInputFilter<EntityServerResource<T>, T>())
                        .calling(new FormDataExtractingFilter<EntityServerResource<T>, T>())
                        .calling(new CheckBusinessViolationsFilter<EntityServerResource<T>, T>())
                        .calling(new PersistEntityFilter<EntityServerResource<T>, T>())
                        ;
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
