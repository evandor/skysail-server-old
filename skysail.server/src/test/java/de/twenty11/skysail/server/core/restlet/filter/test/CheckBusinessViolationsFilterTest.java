package de.twenty11.skysail.server.core.restlet.filter.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
import de.twenty11.skysail.server.core.restlet.EntityServerResource;
import de.twenty11.skysail.server.core.restlet.filter.CheckBusinessViolationsFilter;
import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
import de.twenty11.skysail.server.core.restlet.testresources.MyEntityResource;

public class CheckBusinessViolationsFilterTest {

    @Test
    public void should_fail_for_simpleEntity_without_name() {

        CheckBusinessViolationsFilter<EntityServerResource<SimpleEntity>, SimpleEntity> filter = new CheckBusinessViolationsFilter<EntityServerResource<SimpleEntity>, SimpleEntity>();

        Request request = Mockito.mock(Request.class);
        SimpleEntity entity = new SimpleEntity();
        MyEntityResource resource = new MyEntityResource();

        SkysailResponse<SimpleEntity> skysailResponse = new SkysailResponse<SimpleEntity>(entity);
        ResponseWrapper<SimpleEntity> response = new ResponseWrapper<SimpleEntity>(skysailResponse);
        filter.doHandle(resource, request, response);
        assertThat(response.getSkysailResponse().getSuccess(), is(false));
    }

    // @Test
    // public void should_fail_for_simpleEntityList_without_name() {
    //
    // CheckBusinessViolationsFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>> filter = new
    // CheckBusinessViolationsFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>>();
    //
    // Request request = Mockito.mock(Request.class);
    // SimpleEntity entity = new SimpleEntity();
    // MyListResource resource = new MyListResource();
    //
    // List<SimpleEntity> data = new ArrayList<SimpleEntity>();
    // data.add(entity);
    //
    // SkysailResponse<List<SimpleEntity>> skysailResponse = new SkysailResponse<List<SimpleEntity>>(data);
    // ResponseWrapper<List<SimpleEntity>> response = new ResponseWrapper<List<SimpleEntity>>(skysailResponse);
    // filter.doHandle(resource, request, response);
    // assertThat(response.getSkysailResponse().getSuccess(), is(false));
    // }

}
