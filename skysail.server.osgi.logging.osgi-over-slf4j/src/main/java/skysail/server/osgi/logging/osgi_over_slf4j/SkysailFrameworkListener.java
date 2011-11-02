package skysail.server.osgi.logging.osgi_over_slf4j;

import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.slf4j.Logger;

public class SkysailFrameworkListener implements FrameworkListener {

    private static Logger logger;

    public SkysailFrameworkListener(Logger myLogger) {
        SkysailFrameworkListener.logger = myLogger;
    }

    @Override
    public void frameworkEvent(FrameworkEvent event) {
        switch (event.getType()) {
        case FrameworkEvent.ERROR:
            logger.error("Framework ERROR in bundle {}: {}", event.getBundle(), event.getThrowable().getMessage());
            break;
        case FrameworkEvent.INFO:
            logger.info("Framework ERROR in bundle {}", event.getBundle());
            break;
        case FrameworkEvent.PACKAGES_REFRESHED:
            logger.debug("Framework event PACKAGES_REFRESHED in bundle {}", event.getBundle());
            break;
        case FrameworkEvent.STARTED:
            logger.info("bundle {} STARTED", event.getBundle());
            break;
        case FrameworkEvent.STARTLEVEL_CHANGED:
            logger.info("Framework event STARTLEVEL_CHANGED for bundle {}", event.getBundle());
            break;
        case FrameworkEvent.STOPPED:
            logger.info("bundle {} STOPPED", event.getBundle());
            break;
        case FrameworkEvent.WARNING:
            logger.warn("Framework event WARNING in bundle {}", event.getBundle());
            break;
        default:
            logger.error("event {} not logged properly", event.getType());
            break;
        }

    }
}
