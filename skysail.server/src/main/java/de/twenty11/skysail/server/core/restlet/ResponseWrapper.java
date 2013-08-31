package de.twenty11.skysail.server.core.restlet;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class ResponseWrapper<T> {

    private SkysailResponse<T> skysailResponse;

    public ResponseWrapper(SkysailResponse<T> skysailResponse) {
        this.skysailResponse = skysailResponse;
    }

    public SkysailResponse<T> getSkysailResponse() {
        return skysailResponse;
    }

    public void setSkysailResponse(SkysailResponse<T> skysailResponse) {
        this.skysailResponse = skysailResponse;
    }
}
