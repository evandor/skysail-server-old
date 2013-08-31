package de.twenty11.skysail.server.core.restlet.testresources;

import java.util.Arrays;
import java.util.List;

import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;

public class MyListResource extends ListServerResource<SimpleEntity> {

    @Override
    public SimpleEntity getData(Form form) {
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(SimpleEntity entity) {
        return null;
    }

    @Override
    protected List<SimpleEntity> getData() {
        return Arrays.asList(new SimpleEntity("one"), new SimpleEntity("two"));
    }

}
