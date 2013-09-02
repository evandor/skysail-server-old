package de.twenty11.skysail.server.core.restlet;

import org.restlet.data.Method;

import de.twenty11.skysail.server.core.restlet.filter.CheckInvalidInputRequestHandlingFilter;
import de.twenty11.skysail.server.core.restlet.filter.FormDataExtractingRequestFilter;

public class RequestHandler<T> {

    // @formatter:off

    public SkysailRequestHandlingFilter<T> getChain(Method method) {
        if (method.equals(Method.GET)) {
        
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingRequestHandlingFilter<T>()
                        .calling(new QueryExtractingRequestHandlingFilter<T>())
                        .calling(new DataExtractingRequestFilter<T>());
        
        } else if (method.equals(Method.POST)) {

            // T entity = getData(form);
            // Set<ConstraintViolation<T>> violations = validate(entity);
            // if (violations.size() > 0) {
            // return new ConstraintViolationsResponse(entity, getOriginalRef(), violations);
            // }
            // return addEntity(entity);
            
            
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingRequestHandlingFilter<T>()
                        .calling(new CheckInvalidInputRequestHandlingFilter<T>())
                        .calling(new FormDataExtractingRequestFilter<T>())
                        ;
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
