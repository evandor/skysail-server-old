package de.twenty11.skysail.server.management.internal;

import java.lang.management.OperatingSystemMXBean;

import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.core.restlet.UniqueResultServerResource2;
import de.twenty11.skysail.server.internal.Configuration;

public class ServerLoadResource extends UniqueResultServerResource2<Double> {

    private OperatingSystemMXBean managementBeanFromContext;

    @Override
    protected void doInit() throws ResourceException {
        managementBeanFromContext = (OperatingSystemMXBean) getContext().getAttributes().get(
                Configuration.CONTEXT_OPERATING_SYSTEM_BEAN);
    }

    @Override
    protected Double getData() {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) managementBeanFromContext;
        return bean.getSystemLoadAverage();
    }

}
