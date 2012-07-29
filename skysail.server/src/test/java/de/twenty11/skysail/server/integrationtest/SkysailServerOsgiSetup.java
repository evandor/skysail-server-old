package de.twenty11.skysail.server.integrationtest;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.List;

import org.ops4j.pax.exam.Option;

import de.twenty11.skysail.common.osgi.SkysailCommonOsgiSetup;

/**
 * This class defines the bundles skysail.common depends on (used by pax-exam), i.e.
 * the bundles to be used containing the non-optional packages imported by this bundle.
 * 
 * The provided List with options does not contain the current bundle itself!
 * 
 * @author carsten
 *
 */
public class SkysailServerOsgiSetup extends SkysailCommonOsgiSetup {

    @Override
	public List<Option> getOptions() {
        List<Option> options = super.getOptions();
        options.add(mavenBundle("de.twentyeleven.skysail","skysail.common", "0.3.2-SNAPSHOT"));
        return options;
	}
   

}
