package de.twenty11.skysail.server.core.restlet.testresources;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;

public class MyListResource extends ListServerResource<SimpleEntity> {

    private Request request;

    public MyListResource(Form form) {
        request = Mockito.mock(Request.class);
        Reference resourceRef = Mockito.mock(Reference.class);
        Mockito.when(request.getResourceRef()).thenReturn(resourceRef);
        Mockito.when(resourceRef.getQueryAsForm()).thenReturn(form);
    }

    @Override
    public List<SimpleEntity> getData(Form form) {
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(SimpleEntity entity) {
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

    @Override
    public Request getRequest() {
        return request;
    }

}
