package de.twenty11.skysail.server.forms;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.restlet.Application;
import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.common.forms.RestfulForms;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.forms.internal.Activator;
import de.twenty11.skysail.server.forms.internal.FormsModel;
import de.twenty11.skysail.server.forms.internal.FormsSkysailApplication;
import de.twenty11.skysail.server.restlet.ListServerResource;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

/**
 * Restlet Resource class for handling Connections.
 * 
 * Provides a method to retrieve the existing connections and to add a new one.
 * 
 * The managed entity is of type {@link ConnectionDetails}, providing details (like jdbc url, username
 * and password about what is needed to actually connect to a datasource.
 *
 */
public class FormsResource extends ListServerResource<FormDetails> implements RestfulForms {

    /** slf4j based logger implementation */
    private static Logger logger = LoggerFactory.getLogger(FormsResource.class);

    public FormsResource() {
        setName("dbviewer connections resource");
        setDescription("The resource containing the list of connections");
    }

    @Override
    @Get
    public Response<List<FormDetails>> getForms() {
        return getEntities(allForms(), "all forms");
    }

    @SuppressWarnings("unchecked")
    private List<FormDetails> allForms() {
        RestletOsgiApplication application = (RestletOsgiApplication)getApplication();
        String applicationName = application.getApplicationName();
        Map<ApplicationDescriptor, FormsModel> formModels = Activator.getFormModels();
        for (ApplicationDescriptor appDescriptor : formModels.keySet()) {
            if (appDescriptor.getApplicationDescription().getName().equals(applicationName)) {
                FormsModel formsModel = formModels.get(appDescriptor);
                return formsModel.getAllForms();
            }
        }
        return Collections.emptyList();
        
    }

}
