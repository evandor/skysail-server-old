package de.twenty11.skysail.server.integrationtest;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;

import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.osgi.PaxExamOptionSet;
import de.twenty11.skysail.common.osgi.SkysailCommonOsgiSetup;

/**
 * This class defines the bundles skysail.server depends on (used by pax-exam), i.e.
 * the bundles to be used containing the non-optional packages imported by this bundle.
 * 
 * The provided List with options does not contain the current bundle itself!
 * 
 * @author carsten
 *
 */
public class SkysailServerOsgiSetup extends SkysailCommonOsgiSetup {

    private static Logger logger = LoggerFactory.getLogger(SkysailServerOsgiSetup.class.getName());

    @Override
	public List<Option> getOptions(EnumSet<PaxExamOptionSet> optionSets) {
        List<Option> options = super.getOptions(optionSets);
        
        // restlet
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.slf4j", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.freemarker", "2.0.11"));
        //options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.servlet", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xstream", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.jackson", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.wadl", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xml", "2.0.11"));
        options.add(mavenBundle("org.restlet.jee", "org.restlet", "2.0.11"));

        // osgi 
        options.add(mavenBundle("org.osgi", "org.osgi.enterprise", "4.2.0"));
        
        // Declarative Services:
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.log","1.2.100.v20100503"));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0"));
        
        // Felix file install
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.fileinstall", "3.2.4"));

        // other
        options.add(mavenBundle("org.freemarker", "com.springsource.freemarker", "2.3.18"));
        options.add(mavenBundle("com.thoughtworks.xstream", "com.springsource.com.thoughtworks.xstream", "1.3.1"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-core-lgpl", "1.9.5"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-mapper-lgpl", "1.9.5"));
        options.add(mavenBundle("javax.xml.stream", "com.springsource.javax.xml.stream", "1.0.1"));
        options.add(mavenBundle("org.xmlpull","com.springsource.org.xmlpull", "1.1.4.c"));
        options.add(mavenBundle("org.codehaus.jettison", "com.springsource.org.codehaus.jettison", "1.0.1"));

        // skysail.common
        options.add(mavenBundle("de.twentyeleven.skysail","skysail.common", "0.3.5-SNAPSHOT"));

        logger.info ("using options from {} for tests", this.getClass());

        return options;
	}
}
