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

package de.twenty11.skysail.server.serviceprovider.test;

import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.equinox;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.provision;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.bootDelegationPackage;
import static org.ops4j.pax.tinybundles.core.TinyBundles.bundle;

import java.io.InputStream;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;

import de.twenty11.skysail.server.servicedefinitions.ConfigService;
import de.twenty11.skysail.server.serviceprovider.SkysailServiceProvider;

@RunWith(JUnit4TestRunner.class)
public class ServiceProviderTest {

    @Inject
    private BundleContext context;

    @Configuration
    public final Option[] config() {

        InputStream bundleUnderTest = bundle().add(SkysailServiceProvider.class)
                        .set(Constants.BUNDLE_SYMBOLICNAME, "skysail.server.serviceprovider")
                        .set(Constants.EXPORT_PACKAGE, "de.twenty11.skysail.server.serviceprovider")
                        .set(Constants.IMPORT_PACKAGE, "org.osgi.service.component;version=\"[1.1,2)\"").build();

        // @formatter:off
        return options(
                provision(bundleUnderTest),
                mavenBundle("ch.qos.logback","logback-core","0.9.29"),
                mavenBundle("ch.qos.logback","skysail.bundles.logback-classic","0.9.29"),
                //mavenBundle("org.slf4j","slf4j-api","1.6.3"),
                mavenBundle("de.twenty11.skysail","skysail.server.servicedefinitions","0.1.1"),
                mavenBundle("de.twenty11.skysail","skysail.server.configuration.byPropertiesService","0.0.5-SNAPSHOT"),
                //mavenBundle("osgi.enterprise","osgi.enterprise","4.2.0.201003190513"),
                mavenBundle("org.eclipse.equinox","org.eclipse.equinox.ds","1.2.1"),
                mavenBundle("org.eclipse.equinox","org.eclipse.equinox.util","1.0.200"),
                junitBundles(),
                systemProperty("osgi.console").value("6666"),
                equinox().version("3.6.2")
                ,bootDelegationPackage("app")
        );
        // @formatter:on
    }

    @Test
    public void checkConfigServiceExists() {
        ServiceReference serviceReference = context.getServiceReference(ConfigService.class.getName());
        Object service = context.getService(serviceReference);
        assertTrue(service != null);
        assertTrue(service instanceof ConfigService);
    }

    @Test
    public void getConfigServiceFromSkysailServiceProvider() {
        ServiceReference serviceReference = context.getServiceReference(ConfigService.class.getName());
        ConfigService service = (ConfigService) context.getService(serviceReference);
        SkysailServiceProvider provider = new SkysailServiceProvider() {

            @Override
            protected void deactivate(ComponentContext ctxt) {
            }

            @Override
            protected void activate(ComponentContext ctxt) {
            }
        };

        provider.setConfigService(service);
        assertTrue(provider.getConfigService() == service);
    }
}
