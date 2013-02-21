package de.twentyeleven.skysail.server.restlet.test;

import static org.ops4j.pax.exam.Constants.START_LEVEL_SYSTEM_BUNDLES;
import static org.ops4j.pax.exam.Constants.START_LEVEL_TEST_BUNDLE;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackage;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.felix;
import static org.ops4j.pax.exam.CoreOptions.frameworkStartLevel;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.knopflerfish;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.url;
import static org.ops4j.pax.exam.CoreOptions.when;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.testing.utils.PaxExamOptionSet;
import de.twenty11.skysail.common.testing.utils.SkysailCommonOsgiSetup;

public class OsgiTestIntegrationSetup {

    private static Logger logger = LoggerFactory.getLogger(SkysailCommonOsgiSetup.class.getName());

    private static List<Option> defaultOptions = new ArrayList<Option>();

    static {

        defaultOptions.add(bootDelegationPackage("sun.*"));
        defaultOptions.add(cleanCaches());

        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.exam.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.exam.inject.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.extender.service.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.base.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.swissbox.core.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.swissbox.extender.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.swissbox.lifecycle.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.ops4j.pax.swissbox.framework.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(url("link:classpath:META-INF/links/org.apache.geronimo.specs.atinject.link").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));

        // add SLF4J and logback bundles
        defaultOptions.add(mavenBundle("org.slf4j", "slf4j-api", "1.6.1").startLevel(START_LEVEL_SYSTEM_BUNDLES)); // .versionAsInProject().);
        defaultOptions.add(mavenBundle("ch.qos.logback", "logback-core", "0.9.29").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));
        defaultOptions.add(mavenBundle("ch.qos.logback", "logback-classic", "0.9.29").startLevel(
                START_LEVEL_SYSTEM_BUNDLES));

        // add jcl-over-slf4j
        defaultOptions.add(mavenBundle("org.slf4j", "jcl-over-slf4j", "1.6.1").startLevel(START_LEVEL_SYSTEM_BUNDLES));

        // Set logback configuration via system property.
        // This way, both the driver and the container use the same configuration
        defaultOptions.add(systemProperty("logback.configurationFile").value(
                "file:" + PathUtils.getBaseDir() + "/src/test/resources/logback-test.xml"));

        // only for Pax Runner container
        defaultOptions.add(when("paxrunner".equals(System.getProperty("pax.exam.container")))
                .useOptions(
                        url("link:classpath:META-INF/links/org.ops4j.pax.exam.rbc.link").startLevel(
                                START_LEVEL_SYSTEM_BUNDLES),
                        mavenBundle("org.slf4j", "jcl-over-slf4j", "1.6.1").startLevel(START_LEVEL_SYSTEM_BUNDLES),
                        equinox(), felix(), knopflerfish()));

        // not for Pax Runner container
        defaultOptions.add(when(!"paxrunner".equals(System.getProperty("pax.exam.container"))).useOptions(
                frameworkStartLevel(START_LEVEL_TEST_BUNDLE)));

        // junit
        defaultOptions.add(junitBundles());

        // defaultOptions.add(frameworkProperty("osgi.console").value("6666"));
    }
    @Override
    public List<Option> getOptions(EnumSet<PaxExamOptionSet> optionSets) {

        List<Option> options = super.getOptions(optionSets);

        // needed for skysail.common (dependency)
        options.add(mavenBundle("commons-lang", "commons-lang", "2.6"));
        options.add(mavenBundle("log4j", "log4j", "1.2.17"));

        // http://team.ops4j.org/wiki/display/paxurl/Mvn+Protocol#MvnProtocol-configuration
        options.add(systemProperty("org.ops4j.pax.url.mvn.repositories")
                .value("https://oss.sonatype.org/content/groups/public,https://repository.apache.org/content/repositories/releases,http://download.eclipse.org/rt/eclipselink/maven.repo"));
        // felix().version("3.2.2")
        options.add(equinox().version("3.6.2"));

        logger.info("using options from {} for tests", this.getClass());

        // skysail.common
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.common"));

        // restlet
        String restletVersion = "2.0.14";
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.slf4j", restletVersion));
        // options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.servlet", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xstream", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.jackson", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.wadl", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.xml", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet.ext.json", restletVersion));
        options.add(mavenBundle("org.restlet.jee", "org.restlet", restletVersion));

        // JSON
        options.add(mavenBundle("de.twentyeleven.skysail", "org.json-osgi", "20080701"));

        // osgi
        options.add(mavenBundle("org.osgi", "org.osgi.enterprise", "4.2.0"));

        // Declarative Services:
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"));
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"));
        // options.add(mavenBundle("org.eclipse.equinox","org.eclipse.equinox.log","1.2.100.v20100503"));
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.scr", "1.6.0"));

        options.add(mavenBundle("javax.validation", "com.springsource.javax.validation", "1.0.0.GA"));
        options.add(mavenBundle("org.hibernate", "hibernate-validator", "4.3.0.Final"));
        options.add(mavenBundle("org.jboss.logging", "jboss-logging", "3.1.2.GA"));

        // Felix file install
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.fileinstall", "3.2.4"));
        options.add(systemProperty("felix.fileinstall.noInitialDelay").value("true"));
        // /home/carsten/git/skysail-server-ext/skysail.server.ext.dbviewer/etc/pax-runner/config
        options.add(systemProperty("felix.fileinstall.dir").value(System.getProperty("skysailConfDir", "./conf/dev/")));
        options.add(systemProperty("felix.fileinstall.log.level").value("4"));

        // eclipselink
        options.add(mavenBundle("org.eclipse.persistence", "javax.persistence", "2.0.3"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.core", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.jpa", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.asm", "2.2.0"));
        options.add(mavenBundle("org.eclipse.persistence", "org.eclipse.persistence.osgi", "2.2.0"));
        // options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server.eclipselink", "0.0.3-SNAPSHOT"));

        // other
        options.add(mavenBundle("com.thoughtworks.xstream", "com.springsource.com.thoughtworks.xstream", "1.3.1"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-core-lgpl", "1.9.5"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-mapper-lgpl", "1.9.5"));
        options.add(mavenBundle("javax.xml.stream", "com.springsource.javax.xml.stream", "1.0.1"));
        options.add(mavenBundle("org.xmlpull", "com.springsource.org.xmlpull", "1.1.4.c"));
        options.add(mavenBundle("org.codehaus.jettison", "com.springsource.org.codehaus.jettison", "1.0.1"));

        options.add(mavenBundle("commons-dbcp", "commons-dbcp", "1.4"));
        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.fragment.commons.dbcp", "0.0.1-SNAPSHOT").noStart());
        options.add(mavenBundle("commons-pool", "commons-pool", "1.6"));

        options.add(mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.4.0 "));

        logger.info("using options from {} for tests", this.getClass());

        options.add(mavenBundle("de.twentyeleven.skysail", "skysail.server", "0.2.5-SNAPSHOT"));

        // restassured:
        options.add(mavenBundle("de.twentyeleven.skysail", "com.jayway.rest-assured-osgi", "1.6.2"));
        options.add(mavenBundle("de.twentyeleven.skysail", "org.hamcrest.hamcrest-all-osgi", "1.3.0.1"));
        options.add(mavenBundle("de.twentyeleven.skysail", "org.ccil.cowan.tagsoup-osgi", "1.2.1"));
        options.add(mavenBundle("commons-collections", "commons-collections", "3.2.1"));
        options.add(mavenBundle("org.apache.commons", "commons-lang3", "3.1"));
        options.add(mavenBundle("org.apache.httpcomponents", "httpcore-osgi", "4.1.4"));
        options.add(mavenBundle("org.apache.httpcomponents", "httpclient-osgi", "4.1.3"));
        options.add(mavenBundle("org.codehaus.groovy", "groovy-all", "1.8.4"));
        options.add(mavenBundle("org.codehaus.jackson", "jackson-core-lgpl", "1.9.5"));

        // mysql
        options.add(mavenBundle("mysql", "skysail.bundles.mysql-connector-java", "5.1.6"));

        // felix config admin
        options.add(mavenBundle("org.apache.felix", "org.apache.felix.configadmin", "1.4.0"));

        options.add(mavenBundle("commons-dbcp", "skysail.bundles.commons-dbcp", "1.4"));

        logger.info("using options from {} for tests", this.getClass());

        return options;
    }

}
