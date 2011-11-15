package de.skysail.server.osgi.hibernate.session.internal;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

import de.twenty11.skysail.server.EntityService;

public class EntityServiceTracker extends ServiceTracker {

    public EntityServiceTracker(BundleContext context) {
        super(context, EntityService.class.getName(), null);
    }

    @Override
    public Object addingService(ServiceReference reference) {
        String annotatedEntity = (String) reference.getProperty("annotatedEntity");
        if (annotatedEntity != null) {
            String[] classes = new String[1];
            classes[0] = annotatedEntity;
            HibernateSessionActivator.updateBundleClasses(reference.getBundle(), classes, ServiceEvent.REGISTERED);
        }
        return reference;
    }

    @Override
    public void remove(ServiceReference reference) {
        String annotatedEntity = (String) reference.getProperty("annotatedEntity");
        if (annotatedEntity != null) {
            String[] classes = new String[1];
            classes[0] = annotatedEntity;
            HibernateSessionActivator.updateBundleClasses(reference.getBundle(), classes, ServiceEvent.UNREGISTERING);
        }
    }

}
