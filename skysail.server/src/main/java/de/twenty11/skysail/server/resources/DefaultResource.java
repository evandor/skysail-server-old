package de.twenty11.skysail.server.resources;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Application;
import org.restlet.data.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class DefaultResource extends de.twenty11.skysail.server.core.restlet.ListServerResource<String> {

    private static Logger logger = LoggerFactory.getLogger(DefaultResource.class);

    private List<Application> applications = new ArrayList<Application>();

    public DefaultResource() {
        setName("Skysail Server Available Applications Resource");
        setDescription("The resource containing the list of available applications");
    }

    @Override
    public List<String> getData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String> getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getMessage(String key) {
        return "Welcome";
    }

    @Override
    public SkysailResponse<?> addEntity(List<String> entity) {
        // TODO Auto-generated method stub
        return null;
    }

    // @Get("html|json")
    // public SkysailResponse<List<AvailableApplication>> getApplications() {
    // SkysailResponse<List<AvailableApplication>> entities = getEntities("test");
    // entities.setPresentationStyleHint(PresentationStyle.LIST);
    // return entities;
    // }
    //
    // private List<AvailableApplication> allApplications() {
    // List<AvailableApplication> result = new ArrayList<AvailableApplication>();
    // for (Application application : applications) {
    // if (application.getName().equals("static")) {
    // continue;
    // }
    // AvailableApplication descriptor = new AvailableApplication(application);
    // result.add(descriptor);
    // }
    // return result;
    // }
    //
    // @Override
    // protected void doInit() throws ResourceException {
    // SkysailApplication app = (SkysailApplication) getApplication();
    // BundleContext bundleContext = app.getBundleContext();
    // applications = ApplicationsService.getApplications(bundleContext);
    // }

}
