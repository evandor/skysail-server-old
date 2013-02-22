package de.twentyeleven.skysail.server.restlet.test;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.bundle;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

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

import de.twenty11.skysail.common.testing.utils.OsgiTestingUtils;
import de.twenty11.skysail.common.testing.utils.PaxExamOptionSet;

/**
 * @author carsten
 * 
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class SkysailServerRestletOsgiIT {

    private List<PaxExamOptionSet> dependencies = new ArrayList<PaxExamOptionSet>();
    private SkysailServerRestletOsgiSetup setup;

    @Inject
    private BundleContext context;

    @Configuration
    public Option[] config() {

        dependencies.add(PaxExamOptionSet.BASE);
        dependencies.add(PaxExamOptionSet.DEBUGGING);

        setup = new SkysailServerRestletOsgiSetup();
        List<Option> options = setup.getOptions(EnumSet.copyOf(dependencies));

        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.common.config.configadmin"));
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.common.ext.osgimonitor"));
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.ext.osgimonitor"));

        // _this_ bundle from target directory
        options.add(bundle("file:target/skysail.server.restlet-" + setup.getProjectVersion() + ".jar"));

        options.add(systemProperty("ds.loglevel").value("4"));

        Option[] options2Use = options.toArray(new Option[options.size()]);
        setup.logOptionsUsed(options2Use);
        return options2Use;
    }

    @Test
    public void shouldFindSomeBundlesInActiveState() {
        OsgiTestingUtils.dumpBundleInfo(context);

        OsgiTestingUtils.dumpServicesInfo(context);

        Bundle bundle = OsgiTestingUtils.getBundleForSymbolicName(context, "skysail.server");
        assertTrue(bundle != null);
        assertTrue(bundle.getState() == 32);

        bundle = OsgiTestingUtils.getBundleForSymbolicName(context, "skysail.server.restlet");
        assertTrue(bundle != null);
        assertTrue(bundle.getState() == 32);
    }

}
