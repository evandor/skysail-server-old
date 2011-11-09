package de.twenty11.skysail.server.osgi;

import java.security.Principal;
import java.util.List;

import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.server.EntityService;

public interface IComponentsLookup {

    List<TreeNodeData> getComponents(Principal userPrincipal);

    EntityService getComponent(String name);
    
    Object getImage(String componentName, String identifier, String size);

}
