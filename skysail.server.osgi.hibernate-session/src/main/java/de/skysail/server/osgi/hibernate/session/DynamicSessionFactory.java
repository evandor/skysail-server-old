package de.skysail.server.osgi.hibernate.session;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Hibernate session factory that can get updated during the runtime of the application.
 */
public class DynamicSessionFactory implements FactoryBean, InitializingBean {

	private SessionFactoryProvider sessionFactoryProvider;
	private String identifier;

    public DynamicSessionFactory() {
        System.out.println("new DynamicSessionFactory instantiated");
    }

	public void setConfiguration(SessionFactoryProvider sfProvider) {
		this.sessionFactoryProvider = sfProvider;
	}
	
	public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

	public Object getObject() throws Exception {
		return sessionFactoryProvider.getSessionFactory();
	}

	public Class getObjectType() {
		return null;
	}

	public boolean isSingleton() {
		return true;
	}

	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public String toString() {
	    return new StringBuffer ("['").append(identifier).append("', ").
	            append(sessionFactoryProvider == null ? "null" : sessionFactoryProvider.toString()).
	            append("]").toString();
	}
}
