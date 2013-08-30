package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class ResponseWrapper<T> {

    private SkysailResponse skysailResponse;

    public ResponseWrapper(SkysailResponse skysailResponse) {
        this.skysailResponse = skysailResponse;
    }
    
    public SkysailResponse getSkysailResponse() {
        return skysailResponse;
    }
    
    public void setSkysailResponse(SkysailResponse skysailResponse) {
        this.skysailResponse = skysailResponse;
    }
}
