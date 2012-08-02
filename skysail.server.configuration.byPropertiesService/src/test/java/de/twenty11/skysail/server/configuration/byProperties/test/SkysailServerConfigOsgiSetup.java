package de.twenty11.skysail.server.configuration.byProperties.test;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;

import de.twenty11.skysail.common.osgi.PaxExamOptionSet;
import de.twenty11.skysail.server.integrationtest.SkysailServerOsgiSetup;

/**
 * This class defines the bundles skysail.server depends on (used by pax-exam), i.e.
 * the bundles to be used containing the non-optional packages imported by this bundle.
 * 
 * The provided List with options does not contain the current bundle itself!
 * 
 * @author carsten
 *
 */
public class SkysailServerConfigOsgiSetup extends SkysailServerOsgiSetup {

    @Override
	public List<Option> getOptions(EnumSet<PaxExamOptionSet> optionSets) {
        List<Option> options = super.getOptions(optionSets);
        
        options.add(mavenBundle("de.twentyeleven.skysail","skysail.server", "0.2.1-SNAPSHOT"));

        // ds:
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.log","1.2.100.v20100503"));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0"));

        return options;
	}
}
