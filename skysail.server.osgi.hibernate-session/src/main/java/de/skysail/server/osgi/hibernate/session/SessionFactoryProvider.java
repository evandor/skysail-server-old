package de.skysail.server.osgi.hibernate.session;

import org.hibernate.SessionFactory;

/**
 * Generic Interface to mark classes providing - some way or the other - hibernate 
 * session factories.
 * 
 * @author carsten
 *
 */
public interface SessionFactoryProvider {

	/**
	 * main interface method - getSessionFactory.
	 * 
	 * @return provided hibernate sessionFactory
	 */
	public abstract SessionFactory getSessionFactory();

}