package de.twenty11.skysail.server.um.resources.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.ResourceTestWithUnguardedAppication;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailUser;
import de.twenty11.skysail.server.um.repos.UserRepository;
import de.twenty11.skysail.server.um.resources.UsersResource;

public class UsersResourceTest extends ResourceTestWithUnguardedAppication<UserManagementApplication> {
	
	private UsersResource resource;
	private UserManagementApplication spy;
	private UserRepository userRepository;

	@Before
	public void setUp() throws Exception {
		spy = setUpApplication(new UserManagementApplication());
		userRepository = Mockito.mock(UserRepository.class);
		resource = new UsersResource();
	}

	@Test
	public void testName() throws Exception {

		Mockito.doAnswer(new Answer<UserRepository>() {
			@Override
			public UserRepository answer(InvocationOnMock invocation) throws Throwable {
				return userRepository;
			}
		}).when(spy).getUserRepository();

		SkysailResponse<List<SkysailUser>> entities = resource.getEntities();
		System.out.println(entities);
	}

}
