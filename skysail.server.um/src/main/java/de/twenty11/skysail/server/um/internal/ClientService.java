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
//package de.twenty11.skysail.server.um.internal;
//
//import java.util.Properties;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//
//import org.eclipse.persistence.config.PersistenceUnitProperties;
//import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
//
//import de.twenty11.skysail.server.servicedefinitions.ConfigService;
//
//public class ClientService {
//
//    public static SkysailClient getClient(int i) {
//        EntityManager em = getEntityManager();
//        return em.find(SkysailClient.class, i);
//    }
//
//    private static EntityManager getEntityManager() {
//        ConfigService configService = ServiceProvider.getConfigService();
//        Properties properties = configService.getProperties("skysail.defaultDb.");
//        properties.put(PersistenceUnitProperties.CLASSLOADER, ClientService.class.getClassLoader());
//        EntityManagerFactory emf = new PersistenceProvider()
//                        .createEntityManagerFactory("skysail.server.um", properties);
//        return emf.createEntityManager();
//    }
// }
