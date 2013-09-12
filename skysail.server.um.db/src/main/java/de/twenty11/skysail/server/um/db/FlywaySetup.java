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
package de.twenty11.skysail.server.um.db;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.dbcp.BasicDataSource;

import com.googlecode.flyway.core.Flyway;

import de.twenty11.skysail.server.services.ServiceIsInitializedIndicator;

public class FlywaySetup implements ServiceIsInitializedIndicator {

    private EntityManagerFactory enitityManagerFactory;

    public void init() {
        Flyway flyway = new Flyway();

        Map<?, ?> properties = (Map<?, ?>) enitityManagerFactory.getProperties().get("PUnitInfo");

        BasicDataSource bds = new BasicDataSource();

        bds.setUrl((String) properties.get("driverUrl"));
        bds.setPassword((String) properties.get("driverPassword"));
        bds.setUsername((String) properties.get("driverUser"));
        bds.setDriverClassName((String) properties.get("driverClassName"));

        flyway.setDataSource(bds);
        flyway.setTable("skysail_server_um_schema_version");
        flyway.setLocations("dbmig/server_um/");

        ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        ClassLoader thisClassLoader = this.getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(thisClassLoader);

        flyway.setInitOnMigrate(true);
        flyway.migrate();
        Thread.currentThread().setContextClassLoader(ccl);

    }

    public synchronized void setEntityManager(EntityManagerFactory emf) {
        this.enitityManagerFactory = emf;
    }
}
