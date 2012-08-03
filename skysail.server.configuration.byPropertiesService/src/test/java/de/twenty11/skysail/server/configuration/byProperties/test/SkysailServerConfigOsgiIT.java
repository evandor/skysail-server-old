package de.twenty11.skysail.server.configuration.byProperties.test;

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

import de.twenty11.skysail.common.osgi.PaxExamOptionSet;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class SkysailServerConfigOsgiIT {

	private List<PaxExamOptionSet> dependencies = new ArrayList<PaxExamOptionSet>();

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {
    	
		dependencies.add(PaxExamOptionSet.BASE);
		dependencies.add(PaxExamOptionSet.DEBUGGING);

        SkysailServerConfigOsgiSetup setup = new SkysailServerConfigOsgiSetup();
        List<Option> options = setup.getOptions(EnumSet.copyOf(dependencies));
        
        // _this_ bundle from target directory
        options.add(bundle("file:target/skysail.server.configuration.byPropertiesService-"+setup.getProjectVersion()+".jar"));
        
        return options.toArray(new Option[options.size()]);
    }

    @Test
    public void shouldFindCommonBundleInActiveState() {
        Bundle skysailCommonBundle = null;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals("skysail.server.configuration.byPropertiesService")) {
                skysailCommonBundle = bundle;
            }
        }
        assertTrue(skysailCommonBundle != null);
        assertTrue(skysailCommonBundle.getState() == 32);
    }
    
    @Test
    public void shouldProvideConfigService() {
        Bundle skysailCommonBundle = null;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals("skysail.server.configuration.byPropertiesService")) {
                skysailCommonBundle = bundle;
            }
        }
        ServiceReference sr = skysailCommonBundle.getRegisteredServices()[0];
        String[] propertyKeys = sr.getPropertyKeys(); // [component.id, component.name, objectClass, service.id]
        Bundle[] usingBundles = sr.getUsingBundles();
        assertTrue(skysailCommonBundle.getRegisteredServices().length == 1);
        String[] serviceInterfaces = (String[])sr.getProperty("objectClass");
        assertTrue(serviceInterfaces[0].equals("de.twenty11.skysail.server.services.ConfigService"));
    }

}
