package de.twenty11.skysail.server.osgi.um.metadata

import java.util.List
import org.springframework.orm.hibernate3.support.HibernateDaoSupport

class MetadataDao extends HibernateDaoSupport {
	
	def save(entity: Metadata)         = getHibernateTemplate().save(entity)
	def load(id: Long): Metadata       = getHibernateTemplate().load(classOf[Metadata], id)
	def get(id: Long):Metadata         = getHibernateTemplate().get(classOf[Metadata], id)
	def update(entity: Metadata): Unit = getHibernateTemplate().update(entity)
	def delete(entity: Metadata): Unit = getHibernateTemplate().delete(entity)
	def list():List[Metadata]          = getHibernateTemplate().findByExample(new Metadata()).asInstanceOf[List[Metadata]];

}
