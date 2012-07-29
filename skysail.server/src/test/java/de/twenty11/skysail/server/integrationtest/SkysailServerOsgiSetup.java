package de.twenty11.skysail.server.integrationtest;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.List;

import org.ops4j.pax.exam.Option;

import de.twenty11.skysail.common.osgi.SkysailCommonOsgiSetup;

public class SkysailServerOsgiSetup extends SkysailCommonOsgiSetup {

    @Override
	public List<Option> getOptions() {
        
        List<Option> options = super.getOptions();
        
        options.add(mavenBundle("de.twentyeleven.skysail","skysail.common", "0.3.2-SNAPSHOT"));
        //options.add(mavenBundle("commons-lang", "commons-lang", "2.6"));
        
        return options;
	}
   

}
