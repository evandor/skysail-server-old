package de.twenty11.skysail.server.resources;

import org.restlet.data.Form;
import org.restlet.ext.servlet.ServletUtils;

import de.twenty11.skysail.common.responses.FormResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.AddServerResource2;
import de.twenty11.skysail.server.domain.Credentials;
import de.twenty11.skysail.server.internal.DefaultSkysailApplication;
import de.twenty11.skysail.server.restlet.DefaultResource;
import de.twenty11.skysail.server.security.AuthenticationService;

public class LoginResource extends AddServerResource2<Credentials>{

    @Override
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(new Credentials(),"login");
    }

    @Override
    public Credentials getData(Form form) {
        return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Override
    public SkysailResponse<?> addEntity(Credentials entity) {
        AuthenticationService authService = ((DefaultSkysailApplication)getApplication()).getAuthenticationService();
        try {
//            authService.setRequest(ServletUtils.getRequest(getRequest()));
//            authService.setResponse(ServletUtils.getResponse(getResponse()));
            authService.login(entity.getUsername(), entity.getPassword(),getRequest(), getResponse());
        } catch (Exception e) {
            return new DefaultResource().getApplications();
        }
        return new DefaultResource().getApplications();
    }

}
