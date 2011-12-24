package de.twenty11.skysail.server.osgi.bundles.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.umd.cs.findbugs.annotations.NoWarning;


/**
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** slf4j based logger. */
    private static Logger       logger = LoggerFactory.getLogger(Activator.class);

    /** OSGi bundle context . */
    private static BundleContext context;

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
     * )
     */
    @Override
    @NoWarning("findbug warning not relevant here")
    public final void start(final BundleContext context) throws Exception {
        this.context = context;
        Bundles.getInstance().setContext(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    @NoWarning("findbug warning not relevant here")
    public final void stop(final BundleContext context) throws Exception {
        this.context = null;
    }
    
    public static BundleContext getContext() {
        return context;
    }

}
