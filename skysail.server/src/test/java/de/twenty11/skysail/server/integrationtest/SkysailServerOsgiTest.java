package de.twenty11.skysail.server.integrationtest;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

import java.io.InputStream;
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
import org.osgi.framework.Constants;

import de.twenty11.skysail.server.internal.Activator;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class SkysailServerOsgiTest {

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {
        SkysailServerOsgiSetup setup = new SkysailServerOsgiSetup();
        List<Option> options = setup.getOptions();
        
        //options.add(mavenBundle("de.twentyeleven.skysail","skysail.common", "0.3.2-SNAPSHOT"));

        InputStream bundleUnderTest = bundle().add(Activator.class)
                .set(Constants.BUNDLE_SYMBOLICNAME, "skysail.server")
                //.set(Constants.IMPORT_PACKAGE, "de.twenty11.skysail.common.test")
                .build();
        options.add(provision(bundleUnderTest));
        return options.toArray(new Option[options.size()]);
    }

    @Test
    public void shouldFindCommonBundleInActiveState() {
        Bundle skysailCommonBundle = null;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals("skysail.common")) {
                skysailCommonBundle = bundle;
            }
        }
        assertTrue(skysailCommonBundle != null);
        assertTrue(skysailCommonBundle.getState() == 32);

    }

}
