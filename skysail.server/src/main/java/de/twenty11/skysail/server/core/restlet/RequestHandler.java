package de.twenty11.skysail.server.core.restlet;

import org.restlet.data.Method;

public class RequestHandler<T> {

    public SkysailRequestHandlingFilter<T> getChain(Method method) {
        if (method.equals(Method.GET)) {
            // exceptionCatching -> QueryExtracting -> DataExtracting
            return new ExceptionCatchingRequestHandlingFilter<T>().calling(
                    new QueryExtractingRequestHandlingFilter<T>()).calling(new DataExtractingRequestFilter<T>());
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
