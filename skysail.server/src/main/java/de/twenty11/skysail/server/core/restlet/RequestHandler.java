package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.server.core.restlet.filter.CheckBusinessViolationsFilter;

import org.restlet.data.Method;

import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputFilter;
import de.twenty11.skysail.server.core.restlet.filter.DataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter;
import de.twenty11.skysail.server.core.restlet.filter.FormDataExtractingFilter;
import de.twenty11.skysail.server.core.restlet.filter.QueryExtractingFilter;

public class RequestHandler<T> {

    // @formatter:off

    public ResourceFilter<T> getChain(Method method) {
        if (method.equals(Method.GET)) {
        
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter<T>()
                        .calling(new QueryExtractingFilter<T>())
                        .calling(new DataExtractingFilter<T>());
        
        } else if (method.equals(Method.POST)) {

            // T entity = getData(form);

            // return addEntity(entity);
            
            
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingFilter<T>()
                        .calling(new CheckInvalidInputFilter<T>())
                        .calling(new FormDataExtractingFilter<T>())
                        .calling(new CheckBusinessViolationsFilter<T>())
                        ;
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
