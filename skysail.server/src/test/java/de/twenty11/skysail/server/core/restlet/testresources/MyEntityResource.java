package de.twenty11.skysail.server.core.restlet.testresources;

import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;

public class MyEntityResource extends EntityServerResource<SimpleEntity> {

    @Override
    protected void doInit() throws ResourceException {
        // TODO Auto-generated method stub

    }

    @Override
    public SimpleEntity getData() {
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

    @Override
    public SkysailResponse<?> updateEntity(SimpleEntity entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMessage(String key) {
        return "defaultMessge";
    }

}
