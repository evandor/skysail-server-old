package de.twenty11.skysail.server;

import java.util.Collections;
import java.util.Map;

import org.mockito.Mockito;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.ServerResource;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;

import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.security.AuthenticationService;

public class ResourceTestWithUnguardedAppication<T extends SkysailApplication> {

	private T application;

	protected T setUpApplication(T freshApplicationInstance) throws ClassNotFoundException {

		application = freshApplicationInstance;//new UserManagementApplication();
		application.setAuthenticationService(new AuthenticationService() {
			
			@Override
			public void logout() {
			}
			
			@Override
			public void login(String username, String password) {
			}
			
			@Override
			public Authenticator getAuthenticator(Context context) {
				return new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "realm") {
					// will not guard anything
				};
			}
		});
		T spy = Mockito.spy(application);
		Application.setCurrent(spy);
		application.getInboundRoot();
		addMappings();
		return spy;
	}

	protected void addMappings() throws ClassNotFoundException {
		Map<String, String> urlMapping = Collections.emptyMap();// new
																// Constants().provideUrlMapping();
		for (Map.Entry<String, String> mapping : urlMapping.entrySet()) {
			@SuppressWarnings("unchecked")
			Class<? extends ServerResource> resourceClass = (Class<? extends ServerResource>) Class
					.forName(mapping.getValue());
			application.attachToRouter("" + mapping.getKey(), resourceClass);
		}
	}

}
