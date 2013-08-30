package de.twenty11.skysail.server.core.restlet.testresources;

import java.util.Arrays;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Method;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.core.restlet.RequestHandler;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.SkysailRequestHandlingFilter;
import de.twenty11.skysail.server.core.restlet.testentities.Entity;

public class MyListResource extends ListServerResource2<Entity> {

    RequestHandler<Entity> requestHandler;
    
    public MyListResource(RequestHandler<Entity> requestHandler) {
        this.requestHandler = requestHandler;
    }
    
    @Override
    public Entity getData(Form form) {
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(Entity entity) {
        return null;
    }

    @Override
    protected List<Entity> getData() {
        return Arrays.asList(new Entity("one"), new Entity("two"));
    }

    @Override
    protected SkysailResponse<List<Entity>> getEntities(String defaultMsg) {
        SkysailRequestHandlingFilter<Entity> chain = requestHandler.getChain(Method.GET);
        ResponseWrapper<Entity> wrapper = chain.handle(this, getRequest());
        return wrapper.getSkysailResponse();
    }

}
