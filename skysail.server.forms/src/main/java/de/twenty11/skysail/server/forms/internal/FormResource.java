package de.twenty11.skysail.server.forms.internal;

import java.util.List;

import org.restlet.resource.Get;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.common.forms.RestfulForms;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.restlet.ListServerResource;

/**
 * Restlet Resource class for handling Connections.
 * 
 * Provides a method to retrieve the existing connections and to add a new one.
 * 
 * The managed entity is of type {@link ConnectionDetails}, providing details (like jdbc url, username
 * and password about what is needed to actually connect to a datasource.
 *
 */
public class FormResource extends ListServerResource<FormDetails> implements RestfulForms {

    /** slf4j based logger implementation */
    private static Logger logger = LoggerFactory.getLogger(FormResource.class);

    public FormResource() {
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
        //EntityManager em = ((SkysailApplication) getApplication()).getEntityManager();
        return null;//em.createQuery("SELECT c FROM ConnectionDetails c").getResultList();
    }

}
