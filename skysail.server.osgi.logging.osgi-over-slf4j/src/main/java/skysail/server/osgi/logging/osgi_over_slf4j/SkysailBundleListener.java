package skysail.server.osgi.logging.osgi_over_slf4j;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;

public class SkysailBundleListener implements BundleListener {

    private static Logger logger;

    public SkysailBundleListener(final Logger myLogger) {
        SkysailBundleListener.logger = myLogger;
    }
    
    @Override
    public void bundleChanged(BundleEvent event) {
        String typeMessage = null;
        switch (event.getType()) {
        case BundleEvent.INSTALLED:
            typeMessage = "installed";
            break;
        case BundleEvent.LAZY_ACTIVATION:
            typeMessage = "lazy activation";
            break;
        case BundleEvent.RESOLVED:
            typeMessage = "resolved";
            break;
        case BundleEvent.STARTED:
            typeMessage = "started";
            break;
        case BundleEvent.STARTING:
            typeMessage = "starting";
            break;
        case BundleEvent.STOPPED:
            typeMessage = "stopped";
            break;
        case BundleEvent.STOPPING:
            typeMessage = "stopping";
            break;
        case BundleEvent.UNINSTALLED:
            typeMessage = "uninstalled";
            break;
        case BundleEvent.UNRESOLVED:
            typeMessage = "unresolved";
            break;
        case BundleEvent.UPDATED:
            typeMessage = "updated";
            break;
        default:
            typeMessage = "unknown";
            break;
        }
        logger.debug("BundleEvent '{}' received for bundle {}", typeMessage, event.getBundle());
    }
}