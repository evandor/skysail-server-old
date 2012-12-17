package de.twenty11.skysail.server.graphs.internal;

import java.util.Map;

import de.twenty11.skysail.server.services.ApplicationDescriptor;

public interface GraphModelProvider {

    Map<ApplicationDescriptor, GraphsModel> getGraphModels();
    
}
