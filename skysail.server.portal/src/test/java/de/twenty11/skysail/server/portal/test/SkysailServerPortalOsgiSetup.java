package de.twenty11.skysail.server.portal.test;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;

import de.twenty11.skysail.common.osgi.PaxExamOptionSet;
import de.twenty11.skysail.server.integrationtest.SkysailServerOsgiSetup;

public class SkysailServerPortalOsgiSetup extends SkysailServerOsgiSetup {

    @Override
	public List<Option> getOptions(EnumSet<PaxExamOptionSet> optionSets) {
        
        List<Option> options = super.getOptions(optionSets);
        
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server", "0.2.1-SNAPSHOT"));
        
		options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.configuration.byPropertiesService", "0.1.1-SNAPSHOT"));

		// freemarker
		options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.freemarker", "0.1.1-SNAPSHOT"));
		
		// ok... jetty:
		options.add(mavenBundle("org.eclipse.jetty",  "jetty-server","7.5.4.v20111024"));
		options.add(mavenBundle("javax.servlet",	  "com.springsource.javax.servlet","2.5.0"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-continuation","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-http","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-io","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-util","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty.osgi", "jetty-osgi-boot","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-client","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-deploy","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-security","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-servlet","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-servlets","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-webapp","7.5.4.v20111024"));
		options.add(mavenBundle("org.eclipse.jetty", "jetty-xml","7.5.4.v20111024"));
		//options.add(mavenBundle("org.apache.felix",	"org.apache.felix.http.bundle", "2.2.0"));

		// felix config admin
		options.add(mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.4.0"));

		options.add(mavenBundle("commons-dbcp", "skysail.bundles.commons-dbcp", "1.4"));

        return options;
	}
   

}
