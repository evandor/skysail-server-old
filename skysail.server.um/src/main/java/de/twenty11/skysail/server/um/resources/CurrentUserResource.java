package de.twenty11.skysail.server.um.resources;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource2;

@Presentation(preferred = PresentationStyle.LIST2)
public class CurrentUserResource extends UniqueResultServerResource2<Map<String, String>> {

    @Override
    protected void doInit() throws ResourceException {
    }

    @Override
    protected Map<String, String> getData() {
        Map<String, String> result = new HashMap<String, String>();
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        result.put("username", principal == null ? "" : principal.toString());
        return result;
    }

}
