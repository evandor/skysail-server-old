package de.twenty11.skysail.server.integrationtest;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

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

        // skysail.common
        options.add(mavenBundle("de.twentyeleven.skysail","skysail.common"));

        // restlet
        String restletVersion = "2.0.14";
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.slf4j", restletVersion));
        //options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.servlet", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xstream", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.jackson", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.wadl", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xml", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet", restletVersion));

        // osgi 
        options.add(mavenBundle("org.osgi", "org.osgi.enterprise", "4.2.0"));
        
        // Declarative Services:
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"));
        //      options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.log","1.2.100.v20100503"));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0"));
        
        // Felix file install
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.fileinstall", "3.2.4"));
        options.add(systemProperty("felix.fileinstall.noInitialDelay").value("true"));
        ///home/carsten/git/skysail-server-ext/skysail.server.ext.dbviewer/etc/pax-runner/config
        options.add(systemProperty("felix.fileinstall.dir").value(System.getProperty("skysailConfDir", "./conf/dev/")));
        options.add(systemProperty("felix.fileinstall.log.level").value("4"));
        
        // eclipselink
        options.add(mavenBundle("org.eclipse.persistence", "javax.persistence", "2.0.3"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.core", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.jpa", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.asm", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.2.0"));
        //options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.eclipselink", "0.0.3-SNAPSHOT"));
        
        // other
        options.add(mavenBundle("com.thoughtworks.xstream", "com.springsource.com.thoughtworks.xstream", "1.3.1"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-core-lgpl", "1.9.5"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-mapper-lgpl", "1.9.5"));
        options.add(mavenBundle("javax.xml.stream", "com.springsource.javax.xml.stream", "1.0.1"));
        options.add(mavenBundle("org.xmlpull","com.springsource.org.xmlpull", "1.1.4.c"));
        options.add(mavenBundle("org.codehaus.jettison", "com.springsource.org.codehaus.jettison", "1.0.1"));

        options.add(mavenBundle("commons-dbcp", "commons-dbcp", "1.4"));
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.fragment.commons.dbcp", "0.0.1-SNAPSHOT").noStart());
        options.add(mavenBundle("commons-pool", "commons-pool", "1.6"));

        options.add(mavenBundle("org.apache.felix","org.apache.felix.configadmin","1.4.0 "));

        logger.info ("using options from {} for tests", this.getClass());

        return options;
	}
}
