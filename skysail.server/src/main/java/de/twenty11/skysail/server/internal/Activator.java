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

package de.twenty11.skysail.server.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceListener;

import de.twenty11.skysail.server.listener.SkysailServerBundleListener;
import de.twenty11.skysail.server.listener.SkysailServerFrameworkListener;
import de.twenty11.skysail.server.listener.SkysailServerServiceListener;

/**
 * The bundles activator.
 * 
 * @author carsten
 * 
 */
public class Activator implements BundleActivator {

    /** slf4j based logger implementation. */
    // private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** a skysail server global framework listener. */
    private FrameworkListener frameworkLister = new SkysailServerFrameworkListener();

    /** a skysail server global bundle listener. */
    private BundleListener bundleListener = new SkysailServerBundleListener();

    /** a skysail server global bundle listener. */
    private ServiceListener serviceListener = new SkysailServerServiceListener();

    /** {@inheritDoc} */
    public final void start(final BundleContext context) throws Exception {
        // add framework event listener for all skysail server components
        context.addFrameworkListener(frameworkLister);
        // same for bundles
        context.addBundleListener(bundleListener);
        // ... and services
        context.addServiceListener(serviceListener);
        
//        camel = new DefaultCamelContext();
//        camel.addComponent("log", new LogComponent());
//        camel.addComponent("file", new FileComponent());
//        camel.start();
//        
//        sendToCamelLog("started Skysail Server");
    }
    
//    private void sendToCamelLog(String name) {
//        try {
//            // get the log component
//            Component component = camel.getComponent("log");
//
//            // create an endpoint and configure it.
//            // Notice the URI parameters this is a common pratice in Camel to configure
//            // endpoints based on URI.
//            // com.mycompany.part2 = the log category used. Will log at INFO level as default
//            Endpoint endpoint = component.createEndpoint("log:com.mycompany.part2");
//
//            // create an Exchange that we want to send to the endpoint
//            Exchange exchange = endpoint.createExchange();
//            // set the in message payload (=body) with the name parameter
//            exchange.getIn().setBody(name);
//
//            // now we want to send the exchange to this endpoint and we then need a producer
//            // for this, so we create and start the producer.
//            Producer producer = endpoint.createProducer();
//            producer.start();
//            // process the exchange will send the exchange to the log component, that will process
//            // the exchange and yes log the payload
//            producer.process(exchange);
//
//            // stop the producer, we want to be nice and cleanup
//            producer.stop();
//
//
//
//
//        } catch (Exception e) {
//            // we ignore any exceptions and just rethrow as runtime
//            throw new RuntimeException(e);
//
//        }
//    }

    /** {@inheritDoc} */
    public final void stop(final BundleContext context) throws Exception {


        // clean up
        context.removeFrameworkListener(frameworkLister);
        context.removeBundleListener(bundleListener);
        context.removeServiceListener(serviceListener);
    }

}
