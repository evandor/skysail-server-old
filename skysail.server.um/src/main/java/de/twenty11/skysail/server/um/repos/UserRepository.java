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

import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import de.twenty11.skysail.server.domain.repository.AbstractRepository;
import de.twenty11.skysail.server.um.domain.SkysailUser;

/**
 * 
 * @author graefca
 */
public class UserRepository extends AbstractRepository<SkysailUser> {

    public UserRepository(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public SkysailUser getById(Long id) {
        TypedQuery<SkysailUser> query = getEntityManager().createQuery(
                "SELECT c FROM SkysailUser c WHERE c.pid = :pid", SkysailUser.class);
        query.setParameter("pid", id);
        return query.getSingleResult();
    }

    public SkysailUser getByName(String username) {
        return (SkysailUser) getEntityManager().createNamedQuery("findByName").setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<SkysailUser> getEntities() {
        TypedQuery<SkysailUser> query = getEntityManager()
                .createQuery("SELECT c FROM SkysailUser c", SkysailUser.class);
        return query.getResultList();
    }

    @Override
    public void add(SkysailUser entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().persist(entity);
        getEntityManager().getTransaction().commit();
    }

}
