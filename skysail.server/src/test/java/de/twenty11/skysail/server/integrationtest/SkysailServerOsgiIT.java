package de.twenty11.skysail.server.integrationtest;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.inject.Inject;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.osgi.PaxExamOptionSet;

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
    public void shouldFindCommonBundleInActiveState() {
        Bundle skysailServerBundle = getBundleForSymbolicName("skysail.server");
        assertTrue(skysailServerBundle != null);
        assertTrue(skysailServerBundle.getState() == 32);

    }

    @Test
    public void shouldFindSkysailDatasourceService() {
        Bundle bundle = getBundleForSymbolicName("skysail.server");
        ServiceReference skysailDatasourceReference = context
                .getServiceReference("de.twenty11.skysail.server.services.DataSourceProvider");
        assertTrue(skysailDatasourceReference != null);
    }

//    @Test
//    public void shouldFindSkysailEntityManagerProvider() {
//        Bundle bundle = getBundleForSymbolicName("skysail.server");
//        ServiceReference skysailDatasourceReference = context
//                .getServiceReference("de.twenty11.skysail.server.services.EntityManagerProvider");
//        assertTrue(skysailDatasourceReference != null);
//        EntityManagerProvider service = (EntityManagerProvider) context.getService(skysailDatasourceReference);
//        assertTrue(service != null);
//        // EntityManager entityManager = service.getEntityManager("SkysailPU");
//    }

    private Bundle getBundleForSymbolicName(String symbolicName) {
        Bundle myBundle = null;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals(symbolicName)) {
                myBundle = bundle;
            }
        }
        return myBundle;
    }

}
