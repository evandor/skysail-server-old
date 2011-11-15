package de.skysail.server.osgi.um.users.internal;

import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import de.skysail.server.osgi.um.users.User;

/**
 * DataAccessObject for entity "user".
 * 
 */
public class UserDao extends HibernateDaoSupport {
	
	/**
	 * Method save.
	 * @param note User
	 */
	public void save(User entity) {
		getHibernateTemplate().save(entity);
	}

	/**
	 * Method load.
	 * @param id long
	 * @return User
	 */
	public User load(long id) {
		return getHibernateTemplate().load(User.class, id);
	}

	/**
	 * Method get.
	 * @param id long
	 * @return User
	 */
	public User get(long id) {
		return getHibernateTemplate().get(User.class, id);
	}

	/**
	 * Method update.
	 * @param note User
	 */
	public void update(User entity) {
		getHibernateTemplate().update(entity);
	}

	/**
	 * Method delete.
	 * @param note User
	 */
	public void delete(User entity) {
		getHibernateTemplate().delete(entity);
	}
	
	/**
	 * returns all notes in the database
	 * @return List<User>
	 */
	@SuppressWarnings("unchecked")
	public List<User> list() {
		return getHibernateTemplate().findByExample(new User());
	}
}
