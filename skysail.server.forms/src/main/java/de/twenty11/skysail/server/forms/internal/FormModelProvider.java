package de.twenty11.skysail.server.forms.internal;

import java.util.Map;

import de.twenty11.skysail.server.services.ApplicationDescriptor;

public interface FormModelProvider {

    Map<ApplicationDescriptor, FormsModel> getFormModels();
    
}
