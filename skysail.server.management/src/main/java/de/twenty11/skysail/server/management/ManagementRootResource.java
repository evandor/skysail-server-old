package de.twenty11.skysail.server.management;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Restlet;
import org.restlet.resource.Finder;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Route;
import org.restlet.routing.TemplateRoute;
import org.restlet.util.RouteList;

import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.selfdescription.ResourceDetails;
import de.twenty11.skysail.common.selfdescription.RestfulRoot;
import de.twenty11.skysail.server.restlet.ListServerResource;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;
import de.twenty11.skysail.server.restlet.SkysailServerResource2;

/**
 * Restlet Root Resource for dbViewer application.
 * 
 */
public class ManagementRootResource extends ListServerResource<ResourceDetails> implements RestfulRoot {

    public ManagementRootResource() {
        setAutoDescribing(false);
        setName("osgimonitor root resource");
        setDescription("The root resource of the osgimonitor application");
    }
    
    @Override
    @Get
    public Response<List<ResourceDetails>> getMethods() {
        return getEntities(allMethods(), "listing all entry points for the skysail osgimonitor application");
    }

    private List<ResourceDetails> allMethods() {
        List<ResourceDetails> result = new ArrayList<ResourceDetails>();
        RestletOsgiApplication restletOsgiApp = (RestletOsgiApplication) getApplication();
        RouteList routes = restletOsgiApp.getRoutes();
        for (Route route : routes) {
            if (route instanceof TemplateRoute) {
                handleTemplateRoutes(result, route);
            }
        }
        return result;
    }

    private void handleTemplateRoutes(List<ResourceDetails> result, Route route) {
        TemplateRoute tr = (TemplateRoute) route;
        String from = (tr.getTemplate() == null) ? super.toString() : tr.getTemplate().getPattern();
        
        if (!from.contains("{")) { // some link we can acutally follow
            from = getHostRef() + from + "?media=json";
            String to = (tr.getNext() == null) ? "null" : tr.getNext().toString();
            String desc = "no description available";
            if (tr.getNext() != null) {
                Restlet next = tr.getNext();
                if (next instanceof Finder) {
                    Finder finder = (Finder) next;
                    Class<? extends ServerResource> targetClass = finder.getTargetClass();
                    ServerResource serverResource;
                    try {
                        serverResource = targetClass.newInstance();
                        if (serverResource instanceof SkysailServerResource2<?>) {
                            SkysailServerResource2<?> ssr = (SkysailServerResource2<?>) serverResource;
                            desc = ssr.getDescription();
                        }
                    } catch (Exception e) {
                    }
                    next.getFinderClass();
                }
            }
            ResourceDetails resourceDetails = new ResourceDetails(from, to, desc);// + " -> " + to);
            result.add(resourceDetails);
        }
    }
}
