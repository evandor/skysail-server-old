package de.twenty11.skysail.server.core.restlet;

import java.util.List;

import org.restlet.data.Method;

import de.twenty11.skysail.server.core.restlet.filter.AbstractResourceFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckBusinessViolationsFilter;
import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputFilter;
import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter2;
import de.twenty11.skysail.server.core.restlet.filter.FormDataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.PersistEntityFilter;
import de.twenty11.skysail.server.core.restlet.filter.QueryExtractingFilter;

public class RequestHandler<T> {

    // @formatter:off

    public AbstractResourceFilter<ListServerResource<T>, List<T>> getChain(Method method) {
        if (method.equals(Method.GET)) {
        
            // exceptionCatching -> QueryExtracting -> DataExtracting
            new ExceptionCatchingFilter2<ListServerResource<T>, List<T>>()
                        //.calling(new QueryExtractingFilter<ListServerResource<T>, T>())
                        //.calling(new DataExtractingFilter<T>())
                        ;
            return null;
        } else if (method.equals(Method.POST)) {

            // exceptionCatching -> QueryExtracting -> DataExtracting
            return  new ExceptionCatchingFilter2<ListServerResource<T>, List<T>>()
                        .calling(new CheckInvalidInputFilter<ListServerResource<T>, List<T>>())
                        .calling(new FormDataExtractingFilter<ListServerResource<T>, List<T>>())
                        .calling(new CheckBusinessViolationsFilter<ListServerResource<T>, List<T>>())
                        .calling(new PersistEntityFilter<ListServerResource<T>, List<T>>())
                        ;
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public AbstractResourceFilter<UniqueResultServerResource<T>, T> getChainForEntity(Method method) {
        if (method.equals(Method.GET)) {
        
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter2<UniqueResultServerResource<T>, T>()
                        .calling(new QueryExtractingFilter<UniqueResultServerResource<T>, T>())
                        //.calling(new DataExtractingFilter<T>())
                    ;
        
        } else if (method.equals(Method.POST)) {

            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter2<UniqueResultServerResource<T>, T>()
                        .calling(new CheckInvalidInputFilter<UniqueResultServerResource<T>, T>())
                        .calling(new FormDataExtractingFilter<UniqueResultServerResource<T>, T>())
                        .calling(new CheckBusinessViolationsFilter<UniqueResultServerResource<T>, T>())
                        .calling(new PersistEntityFilter<UniqueResultServerResource<T>, T>())
                        ;
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
