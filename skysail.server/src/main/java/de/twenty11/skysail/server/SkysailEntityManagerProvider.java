///**
// *  Copyright 2011 Carsten Gräf
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// * 
// */
//
//package de.twenty11.skysail.server;
//
//import javax.persistence.EntityManager;
//
//import de.twenty11.skysail.server.services.EntityManagerProvider;
//
//public class SkysailEntityManagerProvider implements EntityManagerProvider {
//
//    public SkysailEntityManagerProvider() {
//        // TODO Auto-generated constructor stub
//    }
//    
//    @Override
//    public EntityManager getEntityManager(String persistenceUnit) {
//        return null;//getEntityManagerFactory(persistenceUnit);
//    }
//
////    private EntityManager getEntityManagerFactory(String unit) {
////        HashMap properties = new HashMap();
////        
////        BasicDataSource defaultDS = Configuration.getDefaultDS();
////        properties.put(PersistenceUnitProperties.CLASSLOADER, this.getClass().getClassLoader());
////
////        properties.put(PersistenceUnitProperties.JDBC_USER, defaultDS.getUsername());
////        properties.put(PersistenceUnitProperties.JDBC_PASSWORD, defaultDS.getPassword());
////        properties.put(PersistenceUnitProperties.JDBC_URL, defaultDS.getUrl());
////        properties.put(PersistenceUnitProperties.JDBC_DRIVER, defaultDS.getDriverClassName());
////        
////        return null;
//////        EntityManagerFactory emf = new PersistenceProvider().createEntityManagerFactory(unit, properties);
//////        emf.getProperties();
////////        emf.createEntityManager().get
//////        return emf.createEntityManager();
////    }
//}
