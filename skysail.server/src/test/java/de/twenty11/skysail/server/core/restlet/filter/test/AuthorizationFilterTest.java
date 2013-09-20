//package de.twenty11.skysail.server.core.restlet.filter.test;
//
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//import org.restlet.Request;
//
//import de.twenty11.skysail.server.core.restlet.ListServerResource;
//import de.twenty11.skysail.server.core.restlet.ResponseWrapper;
//import de.twenty11.skysail.server.core.restlet.filter.AuthorizationFilter;
//import de.twenty11.skysail.server.core.restlet.filter.ExceptionCatchingFilter;
//import de.twenty11.skysail.server.core.restlet.testentities.SimpleEntity;
//import de.twenty11.skysail.server.core.restlet.testresources.MyListResource;
//import de.twenty11.unitprofile.annotations.Profile;
//
//public class AuthorizationFilterTest {
//
//    private AuthorizationFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>> authorizationFilter;
//    private ListServerResource<SimpleEntity> resource;
//    private ExceptionCatchingFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>> outer;
//    private Request request;
//
//    @Before
//    public void setUp() throws Exception {
//        outer = new ExceptionCatchingFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>>();
//        authorizationFilter = new AuthorizationFilter<ListServerResource<SimpleEntity>, List<SimpleEntity>>();
//        outer.calling(authorizationFilter);
//        resource = new MyListResource();
//
//        request = Mockito.mock(Request.class);
//        // ref = Mockito.mock(Reference.class);
//        // myForm = new Form();
//
//    }
//
//    @Test
//    @Profile
//    public void dosomething() {
//        ResponseWrapper<List<SimpleEntity>> wrapper = outer.handle(resource, request);
//        System.out.println(wrapper);
//    }
// }
