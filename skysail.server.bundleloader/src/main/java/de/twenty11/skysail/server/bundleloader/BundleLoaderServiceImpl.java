package de.twenty11.skysail.server.bundleloader;

import java.util.List;

import de.twenty11.skysail.server.servicedefinitions.BundleLoaderService;

import org.osgi.framework.BundleException;
import org.osgi.service.component.ComponentContext;

public class BundleLoaderServiceImpl implements BundleLoaderService {

    private ComponentContext context;

    @Override
    public boolean load(List<String> identifiers) {
        boolean success = true;
        for (String bundlelocation : identifiers) {
            try {
                context.getBundleContext().installBundle(bundlelocation);
            } catch (BundleException e) {
                success = false;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return success;
    }

    protected void activate(ComponentContext ctxt) {
        this.context = ctxt;
    }

    protected void deactivate(ComponentContext ctxt) {
        this.context = null;
    }

}
