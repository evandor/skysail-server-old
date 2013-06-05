package de.twenty11.skysail.server.internal.listener.test;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.framework.BundleEvent;

import de.twenty11.skysail.server.internal.listener.SkysailServerBundleListener;

public class SkysailServerBundleListenerTest {

    private SkysailServerBundleListener skysailServerBundleListener;

    @Before
    public void setUp() {
        skysailServerBundleListener = new SkysailServerBundleListener();
    }

    @Test
    public void testName() {
        BundleEvent event = Mockito.mock(BundleEvent.class);
        skysailServerBundleListener.bundleChanged(event);
        List<BundleEvent> newestEvents = skysailServerBundleListener.getNewestEvents();
        assertThat(newestEvents.size(), is(equalTo(1)));
        assertThat(newestEvents.get(0), is(equalTo(event)));
    }

}
