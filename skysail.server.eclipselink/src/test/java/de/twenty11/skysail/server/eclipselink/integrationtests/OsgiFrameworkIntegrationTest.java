package de.twenty11.skysail.server.eclipselink.integrationtests;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackage;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.vmOption;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

import java.io.InputStream;

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

import de.twenty11.skysail.common.app.SkysailApplication;

@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class OsgiFrameworkIntegrationTest {

    @Inject
    private BundleContext context;

    @Configuration
    public final Option[] config() {

        InputStream bundleUnderTest = bundle().add(SkysailApplication.class)
                .set(Constants.BUNDLE_SYMBOLICNAME, "skysail.common")
                .build();
        
        // @formatter:off
        return options(
                bootDelegationPackage( "sun.*" ),
                cleanCaches(),
                provision(bundleUnderTest),
                junitBundles(),
                
                // needed for console (equinox)
                //bundle("mvn:http://nexus.twentyeleven.de/content/groups/public!org.eclipse.equinox.console/org.eclipse.equinox.console/1.0.0.v20120522-1841"),

                // needed for console (felix)
                bundle("mvn:http://nexus.twentyeleven.de/content/groups/public!org.apache.felix/org.apache.felix.gogo.shell/0.10.0"),
                bundle("mvn:http://nexus.twentyeleven.de/content/groups/public!org.apache.felix/org.apache.felix.gogo.runtime/0.10.0"),
                bundle("mvn:http://nexus.twentyeleven.de/content/groups/public!org.apache.felix/org.apache.felix.gogo.command/0.12.0"),
                bundle("mvn:http://nexus.twentyeleven.de/content/groups/public!org.apache.felix/org.apache.felix.shell/1.0.0"),

                vmOption("-console"),
                // http://team.ops4j.org/wiki/display/paxurl/Mvn+Protocol#MvnProtocol-configuration
                systemProperty("org.ops4j.pax.url.mvn.repositories").value("http://nexus.twentyeleven.de/content/groups/public"),
                systemProperty("osgi.console").value("6666"),
                systemProperty("equinox.ds.debug").value("true"),
                systemProperty("equinox.ds.print").value("true"),
                //equinox().version("3.6.2"),
                felix().version("3.2.2")
        );
        // @formatter:on
    }

    @Test
    public void saveNewFolderSuccessfully() {
    	System.out.println("List of all bundles:");
    	System.out.println("====================");
    	System.out.println();
    	Bundle skysailCommonBundle = null;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
			System.out.print(bundle.getSymbolicName() + " [");
			System.out.print(bundle.getVersion() + "] - ");
			System.out.println(bundle.getState());
			if (bundle.getSymbolicName().equals("skysail.common")) {
				skysailCommonBundle = bundle;
			}
		}
    	System.out.println();
    	assertTrue(skysailCommonBundle != null);
    	assertTrue(skysailCommonBundle.getState() == 32);
    	
    }


}
