/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.logging.startup.tests;

import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;

import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.logging.startup.StartupLogAppender;
import de.twenty11.skysail.logging.startup.internal.Activator;

@RunWith(JUnit4TestRunner.class)
public class PaxTest {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//  group "/osgi/enterprise"            artifact "osgi.enterprise"                      version "4.2.0.201003190513" starting yes,
//  group "/org/eclipse/equinox"        artifact "org.eclipse.equinox.ds"               version "1.2.1"     starting level_1,
//  group "/org/eclipse/equinox"        artifact "org.eclipse.equinox.util"             version "1.0.200"   starting yes,

    @Configuration
    public final Option[] config() {
        // @formatter:off
        return options(
                //mavenBundle("de.twenty11.skysail", "skysail.server.logging.startup",    "0.0.4-SNAPSHOT"),
                //mavenBundle("de.twenty11.skysail", "skysail.server.servicedefinitions", "0.1.1-SNAPSHOT"),
                mavenBundle("ch.qos.logback",      "logback-core",                      "0.9.29"),
                mavenBundle("ch.qos.logback",      "skysail.bundles.logback-classic",   "0.9.29"),
                //mavenBundle("osgi.enterprise",     "osgi.enterprise",                   "4.2.0.201003190513"),
                mavenBundle("org.eclipse.equinox", "org.eclipse.equinox.ds",            "1.2.1"),
                mavenBundle("org.eclipse.equinox", "org.eclipse.equinox.util",          "1.0.200"),
                junitBundles(),
                systemProperty("osgi.console").value("6666"),
                equinox().version("3.6.2")
        );
        // @formatter:on
    }
    
    @Test
    public void dumpLogs() {
        logger.info("testing");
        // these are the loggers something was logged to during startup
//        Set<String> loggerNames = StartupLogAppender.getLoggerNames();
//        // these are the messages we expect to find in the logs
//        Properties expectedMessages = Activator.getExpectedLogMessages();
//
//        // first make sure we have logs for all expected messages
//        Activator.checkLogExistsForAllExpectedMessages(loggerNames, expectedMessages);
//
//        Activator.dumpResults(loggerNames, expectedMessages);
    }
}
