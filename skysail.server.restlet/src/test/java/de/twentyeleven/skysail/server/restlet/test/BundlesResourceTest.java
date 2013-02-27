package de.twentyeleven.skysail.server.restlet.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentMap;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.Request;

import de.twentyeleven.skysail.server.restlet.ApplicationResource;
import de.twentyeleven.skysail.server.restlet.internal.MyApplication;



public class BundlesResourceTest extends BaseTests {
    
    private ApplicationResource resource;

	@Before
    public void setUp() throws Exception {
        MyApplication spy = setUpRestletApplication();
        BundleContext context = mock(BundleContext.class);
        Bundle bundle = mock(Bundle.class);
        when(bundle.getBundleId()).thenReturn(99l);
        when(bundle.getSymbolicName()).thenReturn("symbolic");
        when(bundle.getLastModified()).thenReturn(111l);
        when(bundle.getVersion()).thenReturn(new Version("1.2.3.qualifier"));
        when(bundle.getState()).thenReturn(32);
        Bundle[] bundles = new Bundle[1];
        bundles[0] = bundle;
        when(context.getBundles()).thenReturn(bundles);
        when(spy.getBundleContext()).thenReturn(context);

        resource = new ApplicationResource();
        Request request = mock(Request.class);
        ConcurrentMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();
        attributes.putIfAbsent("bundleId", "99");
        when(request.getAttributes()).thenReturn(attributes);
        resource.init(spy.getContext(), request, null);
    }

    @Test
    public void returns_bundles_with_proper_values() throws Exception {
        List<BundleDescriptor> bundles = getBundles();
        assertThat(bundles.size(), is(equalTo(1)));
        assertThat(bundles.get(0).getVersion(), is(equalTo("1.2.3.qualifier")));
        assertThat(bundles.get(0).getState(), is(equalTo("Active")));
       
    }

    @Test
    @Ignore
    public void gives_error_message_for_post_when_location_doesnt_start_with_prefix() throws Exception {
    	Representation answer = resource.install("wrongLocation");
    	assertThat(answer.getText(), is(equalTo("location didn't start with 'prefix'")));
    }

}
