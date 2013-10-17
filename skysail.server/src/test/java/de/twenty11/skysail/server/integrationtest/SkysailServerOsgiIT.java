package de.twenty11.skysail.server.integrationtest;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.restlet.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.testing.utils.OsgiTestingUtils;
import de.twenty11.skysail.common.testing.utils.PaxExamOptionSet;
import de.twenty11.skysail.server.services.ApplicationProvider;
import de.twenty11.skysail.server.testing.utils.SkysailServerOsgiSetup;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class SkysailServerOsgiIT {

    private static Logger logger = LoggerFactory.getLogger(SkysailServerOsgiIT.class.getName());

    private List<PaxExamOptionSet> dependencies = new ArrayList<PaxExamOptionSet>();

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {

        dependencies.add(PaxExamOptionSet.BASE);
        dependencies.add(PaxExamOptionSet.DEBUGGING);

        SkysailServerOsgiSetup setup = new SkysailServerOsgiSetup();
        List<Option> options = setup.getOptions(EnumSet.copyOf(dependencies));

        // _this_ bundle from target directory
        String currentBundleSource = "file:target/skysail.server-" + setup.getProjectVersion() + ".jar";
        logger.info("adding {} to tests...", currentBundleSource);
        options.add(bundle(currentBundleSource));

        return options.toArray(new Option[options.size()]);
    }

    @Test
    public void shouldFindServerBundleInActiveState() {
        Bundle skysailServerBundle = OsgiTestingUtils.getBundleForSymbolicName(context, "skysail.server");
        assertTrue(skysailServerBundle != null);
        assertTrue(skysailServerBundle.getState() == 32);

    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void shouldReceiceFrameworkEvents() throws Exception {
        Dictionary props = new Hashtable();
        props.put(EventConstants.EVENT_TOPIC, new String[] { "org/osgi/framework/BundleEvent/*" });
        DummyEventHandler myEventHandler = new DummyEventHandler();
        context.registerService(EventHandler.class.getName(), myEventHandler, props);
        Bundle skysailServerBundle = OsgiTestingUtils.getBundleForSymbolicName(context, "skysail.server");
        skysailServerBundle.stop();
        skysailServerBundle.start();
        assertTrue(myEventHandler.getCounter() > 0);
    }

    @Test
    @Ignore
    // not true any more
    public void shouldFindSkysailDatasourceService() {
        Bundle bundle = OsgiTestingUtils.getBundleForSymbolicName(context, "skysail.server");
        assertTrue(bundle != null);
        ServiceReference skysailDatasourceReference = context
                .getServiceReference("de.twenty11.skysail.server.services.DataSourceProvider");
        assertTrue(skysailDatasourceReference != null);
    }

    @Test
    public void a() {
        ApplicationProvider dummyApplicationProvider = new ApplicationProvider() {

            @Override
            public Application getApplication() {
                return new Application() {
                    @Override
                    public String getAuthor() {
                        return "author";
                    }
                };
            }
        };
        assertTrue(dummyApplicationProvider != null);

        context.registerService(ApplicationProvider.class.getName(), dummyApplicationProvider, null);
        ServiceReference serviceReference = context.getServiceReference(ApplicationProvider.class.getName());
        ApplicationProvider service = (ApplicationProvider) context.getService(serviceReference);
        Application applicationFromService = service.getApplication();

        assertTrue(applicationFromService.getAuthor().equals("author"));
    }

    public class DummyEventHandler implements EventHandler {

        int counter = 0;

        @Override
        public void handleEvent(Event event) {
            counter++;
        }

        public int getCounter() {
            return counter;
        }
    }

}
