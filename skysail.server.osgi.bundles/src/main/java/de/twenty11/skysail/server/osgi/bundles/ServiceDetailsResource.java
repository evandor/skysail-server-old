package de.twenty11.skysail.server.osgi.bundles;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.FormData;
import de.twenty11.skysail.common.messages.TextFieldData;
import de.twenty11.skysail.server.osgi.bundles.internal.Activator;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class ServiceDetailsResource extends SkysailServerResource<FormData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public ServiceDetailsResource() {
        setTemplate("skysail.server.osgi.bundles:service.ftl");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.twenty11.skysail.server.restletosgi.SkysailServerResource#getData()
     * 
     * @see
     * org.eclipse.osgi.framework.internal.core.FrameworkCommandProvider._services
     * (CommandInterpreter)
     */
    @Override
    public FormData getData() {
        
        String serviceId = (String) getRequest().getAttributes().get(OsgiBundlesConstants.SERVICE_ID);
        try {
            ServiceReference[] serviceReferences = Activator.getContext().getServiceReferences(null, null);
            ServiceReference serviceForId;
//            for (ServiceReference serviceReference : serviceReferences) {
//                if (serviceReference)
//            }
            FormData form = new FormData();

            TextFieldData tfd = new TextFieldData("symbolicName", "");
            form.addField(tfd);

            return form;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("Invalid Syntax for filter", e);
        }
        
        
    }

}
