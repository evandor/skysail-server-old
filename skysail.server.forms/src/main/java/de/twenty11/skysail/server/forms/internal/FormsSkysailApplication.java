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

package de.twenty11.skysail.server.forms.internal;

import java.util.Map;

import org.osgi.framework.FrameworkUtil;
import org.restlet.Request;
import org.restlet.Response;

import de.twenty11.skysail.server.listener.SkysailApplicationServiceListener;
import de.twenty11.skysail.server.listener.UrlMappingServiceListener;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

/**
 * @author carsten
 * 
 */
public class FormsSkysailApplication extends RestletOsgiApplication {

    private static FormsSkysailApplication self;
    private static FormModelProvider formModelProvider;

    /**
     * @param staticPathTemplate
     */
    public FormsSkysailApplication(String staticPathTemplate) {
        super(FormsApplicationDescriptor.APPLICATION_NAME, staticPathTemplate);
        setDescription("RESTful DbViewer Form bundle");
        setOwner("twentyeleven");
        self = this;
    }

    /**
     * this is done to give osgi a chance to inject serives to restlet; should be changed to some javax.inject approach
     * (like using InjectedServerResource) once this is available.
     * 
     * @return
     */
    public static FormsSkysailApplication get() {
        return self;
    }

    @Override
    public void handle(Request request, Response response) {
        super.handle(request, response);
    }

    // TODO proper place for this here? what about multiple instances?
    protected void attach() {
        if (FrameworkUtil.getBundle(RestletOsgiApplication.class) != null) {
            new UrlMappingServiceListener(this);
            new SkysailApplicationServiceListener(this);
        }
    }

    public static void setFormModelProvider(FormModelProvider fmp) {
        formModelProvider = fmp;
    }

    public static Map<ApplicationDescriptor, FormsModel> getFormModels() {
        return formModelProvider.getFormModels();
    }

}
