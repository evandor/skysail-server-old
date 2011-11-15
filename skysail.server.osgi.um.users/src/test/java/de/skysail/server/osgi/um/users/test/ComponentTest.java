/**
 * 
 */
package de.skysail.server.osgi.um.users.test;

import junit.framework.TestCase;
import de.skysail.server.osgi.um.users.UserComponentJava;

/**
 * @author carsten
 *
 */
public class ComponentTest extends TestCase {

	/**
	 * @param name
	 */
	public ComponentTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link de.twenty11.skysail.server.osgi.um.users.UserComponentJava#setNoteDao(de.twenty11.skysail.server.osgi.um.users.NoteDao)}.
	 */
	public void testSetNoteDao() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.twenty11.skysail.server.osgi.um.users.UserComponentJava#getIdentifier()}.
	 */
	public void testGetIdentifier() {
//		fail("Not yet implemented");
	}

	
	public void testGetSubMenu() {
//		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link de.twenty11.skysail.server.osgi.um.users.UserComponentJava#runCommand(java.lang.String)}.
	 */
	public void testRunCommand() {
		UserComponentJava ncj = new UserComponentJava();
		//GridInfo runCommand = ncj.runCommand("test");
	}

	/**
	 * Test method for {@link de.twenty11.skysail.server.osgi.um.users.UserComponentJava#execute(java.lang.String, javax.servlet.http.HttpServletRequest)}.
	 */
	public void testExecute() {
//		fail("Not yet implemented");
	}

}
