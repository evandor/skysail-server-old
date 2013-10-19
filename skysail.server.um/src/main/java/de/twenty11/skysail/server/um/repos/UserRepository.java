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
package de.twenty11.skysail.server.um.repos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.twenty11.skysail.server.um.domain.SkysailUser;

/**
 * 
 * @author graefca
 */
public class UserRepository {

    private EntityManager entitiyManager;

    public UserRepository(EntityManagerFactory emf) {
        entitiyManager = emf.createEntityManager();
    }

    private EntityManager getEntityManager() {
        return this.entitiyManager;
    }

    public SkysailUser getById(Long id) {
        TypedQuery<SkysailUser> query = getEntityManager().createQuery(
                "SELECT c FROM SkysailUser c WHERE c.pid = :pid", SkysailUser.class);
        query.setParameter("pid", id);
        return query.getSingleResult();
    }

    public SkysailUser getByName(String username) {
        Query query = getEntityManager().createNamedQuery("findByName").setParameter("username", username);
        @SuppressWarnings("unchecked")
        List<SkysailUser> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        if (results.size() == 1) {
            return results.get(0);
        }
        throw new IllegalStateException("multiple users with name '" + username + "'");
    }

    public List<SkysailUser> getEntities() {
        TypedQuery<SkysailUser> query = getEntityManager()
                .createQuery("SELECT c FROM SkysailUser c", SkysailUser.class);
        return query.getResultList();
    }

    public void add(SkysailUser entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(entity);
        getEntityManager().getTransaction().commit();
    }

}
