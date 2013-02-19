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

package de.twenty11.skysail.server.webapp.internal;

import org.osgi.framework.FrameworkUtil;
import org.restlet.Application;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.routing.Router;

import de.twenty11.skysail.server.directory.ClassLoaderDirectory;
import de.twenty11.skysail.server.directory.CompositeClassLoader;
import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;
import de.twenty11.skysail.server.services.ApplicationProvider;

/**
 * @author carsten
 * 
 */
public class WebappApplication extends RestletOsgiApplication implements ApplicationProvider {

    // non-arg constructor needed for scr
    public WebappApplication() {
        this("dummy");
    }

    /**
     * @param staticPathTemplate
     * @param bundleContext
     */
    public WebappApplication(String staticPathTemplate) {
        super(WebappApplicationDescriptor.APPLICATION_NAME, staticPathTemplate);
        setDescription("Static webapp bundle");
        setOwner("twentyeleven");
        // setBundleContext(bundleContext);
    }

    @Override
    public Restlet createInboundRoot() {

        LocalReference localReference = LocalReference.createClapReference(LocalReference.CLAP_THREAD, "/static/");

        CompositeClassLoader customCL = new CompositeClassLoader();
        customCL.addClassLoader(Thread.currentThread().getContextClassLoader());
        customCL.addClassLoader(Router.class.getClassLoader());
        customCL.addClassLoader(this.getClass().getClassLoader());

        ClassLoaderDirectory staticDirectory = new ClassLoaderDirectory(getContext(), localReference, customCL);

        Router router = new Router(getContext());
        router.attach("/" + WebappApplicationDescriptor.APPLICATION_NAME + "/static", staticDirectory);
        return router;
    }

    @Override
    public Application getApplication() {
        return this;
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
    }

    // TODO proper place for this here? what about multiple instances?
    protected void attach() {
        if (FrameworkUtil.getBundle(RestletOsgiApplication.class) != null) {
            urlMappingServiceListener = new UrlMappingServiceListener(this);
            // new SkysailApplicationServiceListener(this);
        }
    }

}
