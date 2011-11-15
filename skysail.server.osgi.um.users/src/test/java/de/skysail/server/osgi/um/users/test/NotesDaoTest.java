package de.skysail.server.osgi.um.users.test;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import de.skysail.server.osgi.um.users.User;
import de.skysail.server.osgi.um.users.internal.UserDao;

/**
 * @author  carsten
 */
@Transactional
public class NotesDaoTest extends AbstractTransactionalDataSourceSpringContextTests{

	/**
	 */
	@Resource
	private UserDao userDao;
	/**
	 */
	private User noteInDb = new User();
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
//		noteInDb.setNote("existing");
//		noteInDb.setTitle("title");
		userDao.save(noteInDb);
	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] {
				"/bundle-context.xml",
				"/test-context.xml"};
	}

	public void testListEmptyDB() {
		List<User> list = userDao.list();
		assertTrue(list != null);
		assertTrue(list.size() == 1);
	}

//	public void testAdd() {
//		User note = new User();
////		note.setNote("note");
//		userDao.save(note);
//		List<User> list = userDao.list();
//		assertTrue(list != null);
//		assertTrue(note.getId() != null);
////		assertTrue(note.getNote().equals("note"));
//	}

//	public void testLoad() {
//		User note = new User();
////		note.setNote("note");
////		note.setTitle("title");
//		userDao.save(note);
//		
//		// TODO probably that is not actually _written to_ the db (transient)
//		User note2 = userDao.load(note.getId());
//		assertTrue(note2.getId() == note.getId());
////		assertTrue(note2.getTitle().equals(note.getTitle()));
//	}
	
//	public void testUpdate() {
//		List<User> list = userDao.list();
//		assertTrue(list != null);
//		int size = list.size();
//		assertTrue(size > 0);
//		long id = list.get(0).getId();
//		User note = userDao.get(id);
////		note.setTitle("updatedTitle");
//		userDao.save(note);
////		assertTrue(userDao.get(id).getTitle().equals("updatedTitle"));
//	}

//	public void testDelete() {
//		List<User> list = userDao.list();
//		assertTrue(list != null);
//		int size = list.size();
//		assertTrue(size > 0);
//		userDao.delete(list.get(0));
//		assertTrue(userDao.list().size() == size - 1);
//	}

	
}
