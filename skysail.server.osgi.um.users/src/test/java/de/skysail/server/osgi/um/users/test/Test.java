package de.skysail.server.osgi.um.users.test;

import de.skysail.server.osgi.um.users.User;
import junit.framework.TestCase;

public class Test extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTitle() {
		User entity  = new User();
		entity.setLogin("test");
		assertTrue(true);
	}

}
