package de.twenty11.skysail.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

public abstract class SkysailRepository<T> {

    private EntityManager entityManager;

    public SkysailRepository(EntityManagerFactory emf) {
        this.entityManager = emf.createEntityManager();
    }

    protected abstract Class<T> getEntityClass();

    public List<T> getAll() {
        @SuppressWarnings("unchecked")
        List<T> resultList = entityManager.createQuery(getAllQuery()).getResultList();
        List<T> filteredResults = new ArrayList<T>();
        for (T details : resultList) {
            // if (filterMatches(details)) {
            filteredResults.add(details);
            // }
        }
        return filteredResults;
    }

    public T find(String name) {
        TypedQuery<T> query = entityManager.createQuery(getQuery(), getEntityClass());
        query.setParameter("name", name);
        T result = (T) query.getSingleResult();
        return result;
    }

    public void add(T entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    private String getEntityName() {
        return getEntityClass().getName();
    }

    private String getQuery() {
        return "SELECT c FROM " + getEntityName() + " c WHERE c.name = :name";
    }

    private String getAllQuery() {
        return "SELECT c FROM " + getEntityName() + " c";
    }
}
