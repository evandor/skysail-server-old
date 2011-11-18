/*
*  Copyright 2010 Daniel Giribet <dani - calidos.com>
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*      
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*//////////////////////////////////////////////////////////////////////////////

package com.calidos.dani.osgi.freemarker;

import java.util.Hashtable;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.BundleTracker;

import com.calidos.dani.osgi.freemarker.impl.TemplateTracker;

import freemarker.template.Configuration;

/**	Activator that does the following:
*	<ol>
*	<li>Looks for a 'freemarker.template.Configuration' service and if present gets an instance</li>
*	<li>If that is not present creates a default freemarker configuration object with the default
*		object wrapper and no cache refresh</li>
*	<li>With that proceeds to track bundles having templates on them</li>
*	</ol>
*	Please @see {@link TemplateTracker} for details of template tracking and URLs to be used.
*	@author daniel giribet
*//////////////////////////////////////////////////////////////////////////////
public class FreemarkerActivator implements BundleActivator {
	

//	private Configuration	freemarkerConfig;
	private TemplateTracker	templateTracker;
	private BundleTracker	tracker;
	private ServiceRegistration registration;
	

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 *//////////////////////////////////////////////////////////////////////////////
	@Override
	public void start(BundleContext context) throws Exception {
		
		
		//ServiceReference serviceReference = context.getServiceReference(Configuration.class.getName());
		ServiceReference[] serviceReferences = context.getServiceReferences(Configuration.class.getName(), "(preparedConfiguration=true)");
		if (serviceReferences!=null && serviceReferences.length>0) {
			Configuration freemarkerConfig = (Configuration) context.getService(serviceReferences[0]);
			templateTracker = new TemplateTracker(freemarkerConfig);
		} else {	
			templateTracker = new TemplateTracker();
		}
				
		tracker = new BundleTracker(context, Bundle.RESOLVED, templateTracker);
	    tracker.open();
	    
	    // if there is an original service reference object we shouldn't have to publish it again
	    if (serviceReferences==null) {
	    	Hashtable<String, String> props = new Hashtable<String, String>(1);
	    	props.put("dynamicConfiguration", "true");
	    	registration = context.registerService(Configuration.class.getName(), templateTracker.getFreemarkerConfiguration(), props);
	    }
	} // start
	

	/* (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 *//////////////////////////////////////////////////////////////////////////////
	@Override
	public void stop(BundleContext context) throws Exception {

		if (tracker!=null) {
			tracker.close();
		}
		
		if (registration!=null) {
			registration.unregister();
		}
				
	} // stop

}
