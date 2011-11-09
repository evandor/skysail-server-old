package de.twenty11.skysail.server.osgi.internal;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.IComponentsLookup;
import de.twenty11.skysail.server.osgi.PortalConstants;

/**
 * @author  carsten
 */
public class ComponentsLookup implements IComponentsLookup {
    
    private ComponentContext context;
    private final List<TreeNodeData> components = new ArrayList<TreeNodeData>();
    private final Map<String, ServiceReference> serviceReferences = new HashMap<String, ServiceReference>();
	private static Logger logger = LoggerFactory
		.getLogger(SkysailOsgiDispatcherServlet.class);

    public void activate(ComponentContext context) {
    	logger.info("activating components lookup");
        this.context = context;
    }

    public void addComponent(ServiceReference reference) {
        String id = getActionId(reference);
        if (id == null) {
            String symbolicName = getSymbolicName(reference);
            logger.warn("Action service from " + symbolicName
                    + " missing id");
            return;
        }
        synchronized (components) {
            //PortalAction data = new PortalAction(reference);
            TreeNodeData node = new TreeNodeData();
            //node.setIdentifier(id);
            node.setName(id);
            node.setType(getType(reference));
            node.setTooltip(getTooltip(reference));
            node.setLeaf(isLeaf(reference));
            node.setOpenAction(getOpenAction(reference));
            node.setClickAction(getClickAction(reference));
            logger.info("adding component with id " + id);
            components.add(node);
            Collections.sort(components);
        }
        synchronized (serviceReferences) {
            serviceReferences.put(id, reference);
        }
    }

	public void deactivate(ComponentContext context) {
        this.context = null;
    }

    private String getActionId(ServiceReference reference) {
        return (String)reference.getProperty(PortalConstants.COMPONENT_NAME_PARAMETER);
    }
    
    private String getOpenAction(ServiceReference reference) {
        return (String)reference.getProperty(PortalConstants.COMPONENT_OPEN_ACTION_PARAMETER);
    }

    private String getClickAction(ServiceReference reference) {
        return (String)reference.getProperty(PortalConstants.COMPONENT_CLICK_ACTION_PARAMETER);
    }

    private String getTooltip(ServiceReference reference) {
        return (String)reference.getProperty(PortalConstants.COMPONENT_TOOLTIP_PARAMETER);
    }

    private String getType(ServiceReference reference) {
        String type = (String)reference.getProperty(PortalConstants.COMPONENT_TYPE_PARAMETER);
        if (type == null)
        	return "component";
        return type;
	}

    private boolean isLeaf(ServiceReference reference) {
        String result = (String)reference.getProperty(PortalConstants.COMPONENT_LEAF_PARAMETER);
        return result == null ? false : new Boolean(result); 
	}

    private String getSymbolicName(ServiceReference reference) {
        Bundle bundle = reference.getBundle();
        String symbolicName = bundle.getSymbolicName();
        return symbolicName;
    }

    public void removeComponent(ServiceReference reference) {
        Object id = getActionId(reference);
        if (id == null)
            return;
        synchronized (components) {
        	logger.info("removing comonent with id " + id);
            components.remove(id);
        }
    }

    /**
	 * @return
	 */
    @Override
    public List<TreeNodeData> getComponents(Principal user) {
        List<TreeNodeData> result = new ArrayList<TreeNodeData>();
        for (TreeNodeData available : components) {
            if (true) {//PermissionService.permissionGranted(user, "")) {
                result.add(available);
            }
        }
        return result;
    }
    
    @Override
    public EntityService getComponent(String name) {
        ServiceReference serviceReference = serviceReferences.get(name);
        return (EntityService)context.locateService("de.twenty11.skysail.server.osgi.internal.ComponentsLookup", serviceReference);
    }    
    
    public Object getImage(String componentName, String identifier, String size) {
    	return null;
    }

}
