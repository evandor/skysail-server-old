/*
 * Copyright 2013 graefca.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.twenty11.skysail.server.domain.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public abstract class AbstractRepository<T> implements Repository<T> {

    private final EntityManager entitiyManager;

    public AbstractRepository(EntityManagerFactory emf) {
        this.entitiyManager = emf.createEntityManager();
    }

    protected EntityManager getEntityManager() {
        return this.entitiyManager;
    }

    /**
     * Typcially something like
     * 
     * {@code 
     * TypedQuery<Note> query = getEntityManager()
     *           .createQuery("SELECT c FROM Note c WHERE c.pid = :pid", Note.class);
     *   query.setParameter("pid", id);
     *   return (Note) query.getSingleResult(); }
     */
    @Override
    public abstract T getById(Long id);

    /**
     * Typically something like
     * 
     * {@code         TypedQuery<Note> query = getEntityManager()
                .createQuery("SELECT c FROM Note c", Note.class);
        return query.getResultList();}
     */
    @Override
    public abstract List<T> getEntities();

    /**
     * Typically something like
     * 
     * {@code 
     *   entitiyManager.getTransaction().begin();
     *   entitiyManager.persist(entity);
     *   entitiyManager.getTransaction().commit();
     * 
     * }
     */
    @Override
    public abstract void add(T entity);

}
