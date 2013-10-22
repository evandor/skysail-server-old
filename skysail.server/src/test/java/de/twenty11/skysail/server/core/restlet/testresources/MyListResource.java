package de.twenty11.skysail.server.core.restlet.testresources;

import java.util.Arrays;
import java.util.List;

import org.restlet.data.Form;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;

public class MyListResource extends ListServerResource<SimpleEntity> {

    @Override
    public List<SimpleEntity> getData(Form form) {
        return Arrays.asList(new SimpleEntity(form.getFirstValue("name")));
    }

    @Override
    public SkysailResponse<?> addEntity(List<SimpleEntity> entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> updateEntity(List<SimpleEntity> entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<SimpleEntity> getData() {
        return Arrays.asList(new SimpleEntity("one"), new SimpleEntity("two"));
    }

    @Override
    public String getMessage(String key) {
        return "defaultMessge";
    }

}
