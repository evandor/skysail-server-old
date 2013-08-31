package de.twenty11.skysail.server.core.restlet.testresources;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.RequestHandler;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;

public class MyEntityResource extends UniqueResultServerResource<SimpleEntity> {

    private RequestHandler<SimpleEntity> requestHandler;

    public MyEntityResource(RequestHandler<SimpleEntity> requestHandler) {
        this.requestHandler = requestHandler;
    }

    @Override
    protected void doInit() throws ResourceException {
        // TODO Auto-generated method stub

    }

    @Override
    protected SimpleEntity getData() {
        return new SimpleEntity("simple");
    }

    @Override
    public SimpleEntity getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(SimpleEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

}
