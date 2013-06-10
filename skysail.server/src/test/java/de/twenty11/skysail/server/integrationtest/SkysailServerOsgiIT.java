package de.twenty11.skysail.server.integrationtest;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;

import java.util.ArrayList;
import java.util.EnumSet;
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

    // @Test
    // public void shouldFindSkysailEntityManagerProvider() {
    // Bundle bundle = getBundleForSymbolicName("skysail.server");
    // ServiceReference skysailDatasourceReference = context
    // .getServiceReference("de.twenty11.skysail.server.services.EntityManagerProvider");
    // assertTrue(skysailDatasourceReference != null);
    // EntityManagerProvider service = (EntityManagerProvider) context.getService(skysailDatasourceReference);
    // assertTrue(service != null);
    // // EntityManager entityManager = service.getEntityManager("SkysailPU");
    // }

}
