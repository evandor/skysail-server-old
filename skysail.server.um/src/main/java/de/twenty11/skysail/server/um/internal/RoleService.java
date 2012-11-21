/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.server.um.internal;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class RoleService {

    private static EntityManagerFactory emf;

    public SkysailRole getRole(int i) {
        EntityManager em = emf.createEntityManager();
        try {
            //em.getTransaction().begin();
            return em.find(SkysailRole.class,i);
            //em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void setEmf(EntityManagerFactory entityManagerFactory) {
        emf = entityManagerFactory;
    }



}
