package de.twenty11.skysail.server.testing.utils;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.testing.utils.PaxExamOptionSet;
import de.twenty11.skysail.common.testing.utils.SkysailCommonOsgiSetup;

/**
 * This class defines the bundles skysail.server depends on (used by pax-exam), i.e. the bundles to be used containing
 * the non-optional packages imported by this bundle.
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
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.common"));

        // restlet
        String restletVersion = "2.1.0";
        // options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.slf4j", restletVersion));
        // options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.servlet", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.xstream", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.jackson", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.wadl", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.xml", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.json", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.servlet", restletVersion));
        options.add(mavenBundle("org.restlet.osgi", "org.restlet.ext.crypto", restletVersion));

        // JSON
        options.add(mavenBundle("de.twentyeleven.skysail", "org.json-osgi", "20080701"));

        // osgi
        options.add(mavenBundle("org.osgi", "org.osgi.enterprise", "4.2.0"));

        // Declarative Services:
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"));
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"));
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.log","1.2.100.v20100503"));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0"));

        // Event Admin:
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.eventadmin", "1.3.2"));

        options.add(mavenBundle("javax.validation", "com.springsource.javax.validation", "1.0.0.GA"));
        options.add(mavenBundle("org.hibernate", "hibernate-validator", "4.3.0.Final"));
        options.add(mavenBundle("org.jboss.logging", "jboss-logging", "3.1.2.GA"));

        // Felix file install and configadmin
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.4.0 "));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.fileinstall", "3.2.4"));
        options.add(systemProperty("felix.fileinstall.noInitialDelay").value("true"));
        // /home/carsten/git/skysail-server-ext/skysail.server.ext.dbviewer/etc/pax-runner/config
        options.add(systemProperty("felix.fileinstall.dir").value(System.getProperty("skysailConfDir", "./conf/dev/")));
        options.add(systemProperty("felix.fileinstall.log.level").value("4"));

        // eclipselink
        options.add(mavenBundle("org.eclipse.persistence", "javax.persistence", "2.0.3"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.4.2"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.core", "2.4.2"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.jpa", "2.4.2"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.asm", "2.4.2"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.4.2"));
        // "de.twentyeleven.skysail", "skysail.server.eclipselink", "0.0.3-SNAPSHOT"));

        // other
        options.add(mavenBundle("com.thoughtworks.xstream", "com.springsource.com.thoughtworks.xstream", "1.3.1"));
        options.add(mavenBundle("javax.xml.stream", "com.springsource.javax.xml.stream", "1.0.1"));
        options.add(mavenBundle("org.xmlpull", "com.springsource.org.xmlpull", "1.1.4.c"));
        options.add(mavenBundle("org.codehaus.jettison", "com.springsource.org.codehaus.jettison", "1.0.1"));
        options.add(mavenBundle("commons-dbcp", "commons-dbcp", "1.4"));
        // options.add(mavenBundle("org.apache.commons", "com.springsource.org.apache.commons.io", "1.4.0"));
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.fragment.commons.dbcp", "0.0.1").noStart());
        options.add(mavenBundle("commons-pool", "commons-pool", "1.6"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-core-lgpl", "1.9.5"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-mapper-lgpl", "1.9.5"));
        // "org.apache.commons", "com.springsource.org.apache.commons.beanutils", "1.8.3"));
        options.add(mavenBundle("org.apache.commons", "com.springsource.org.apache.commons.collections", "3.2.1"));
        options.add(mavenBundle("org.apache.pdfbox", "pdfbox", "1.8.0"));
        options.add(mavenBundle("org.apache.pdfbox", "fontbox", "1.8.0"));
        options.add(mavenBundle("org.apache.pdfbox", "jempbox", "1.8.0"));

        // stringtemplate
        options.add(mavenBundle("de.twentyeleven.skysail", "org.antlr.stringtemplate-osgi", "4.0.2"));

        // stateless4j
        options.add(mavenBundle("de.twentyeleven.skysail", "com.googlecode.stateless4j-osgi", "1.0.1"));

        // apache beanutils
        // TODO still used?
        // options.add(mavenBundle("de.twentyeleven.skysail", "commons-beanutils-osgi", "1.8.3"));
        // options.add(mavenBundle("org.apache.commons", "com.springsource.org.apache.commons.collections", "3.2.1"));

        // server depends on shiro and shiro depends on server???
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.security.shiro", "0.0.2-SNAPSHOT"));
        options.add(mavenBundle("org.apache.shiro", "shiro-core", "1.2.2"));
        options.add(mavenBundle("org.apache.shiro", "shiro-web", "1.2.2"));
        options.add(mavenBundle("org.apache.aries.blueprint", "org.apache.aries.blueprint", "1.1.0"));
        options.add(mavenBundle("org.apache.aries.proxy", "org.apache.aries.proxy", "1.0.1"));
        options.add(mavenBundle("org.apache.aries", "org.apache.aries.util", "1.1.0"));

        // options.add(mavenBundle("de.twentyeleven.skysail", "org.antlr.runtime-osgi", "4.1"));

        options.add(mavenBundle("com.google.guava", "guava", "14.0.1"));

        options.add(mavenBundle("javax.servlet", "com.springsource.javax.servlet", "2.5.0"));

        // only for (java)Agent for now
        options.add(mavenBundle("org.javassist", "javassist", "3.17.1-GA"));

        logger.info("using options from {} for tests", this.getClass());

        return options;
    }
}
