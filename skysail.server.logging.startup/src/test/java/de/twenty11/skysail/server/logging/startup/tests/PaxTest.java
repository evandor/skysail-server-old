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

import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.equinox;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;

@RunWith(JUnit4TestRunner.class)
public class PaxTest {

    @Configuration
    public final Option[] config() {
        // @formatter:off
        return options(
                mavenBundle("de.twenty11.skysail", "skysail.server.logging.startup",    "0.0.4-SNAPSHOT"),
                mavenBundle("de.twenty11.skysail", "skysail.server.servicedefinitions", "0.1.1-SNAPSHOT"),
                mavenBundle("ch.qos.logback",      "logback-core",                      "0.9.29"),
                mavenBundle("ch.qos.logback",      "skysail.bundles.logback-classic",   "0.9.29"),
                mavenBundle("org.slf4j",           "slf4j-api",                         "1.6.3"),
                // mavenBundle("org.ops4j.pax.exam","pax-exam-junit","2.2.0"),
                // TODO make maven bundle
                // bundle("file:///home/carsten/workspaces/skysale2/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/freemarker_2.3.16.jar"),
                // scanDir("/home/carsten/workspaces/skysale2/skysail.server.osgi.ext.freemarker"),
                junitBundles(),
                equinox().version("3.6.2")
        );
        // @formatter:on
//        return options(
//                scanDir("C:/workspaces/skysail/skysail.server.restlet/src/main/webapp/WEB-INF/eclipse/plugins/skysail/").filter("*.jar"),
//                junitBundles()
//        // equinox().version("3.6.2")
//        );
    }
    
    @Test
    public void getDefaultTemplate() {
       
        System.out.println("hi");
    }
}
