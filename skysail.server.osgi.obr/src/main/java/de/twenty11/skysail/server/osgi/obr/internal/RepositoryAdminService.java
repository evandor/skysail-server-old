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

package de.twenty11.skysail.server.osgi.obr.internal;

import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.servicedefinitions.ConfigService;

public class RepositoryAdminService {

    /** slf4j based logger impl. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static RepositoryAdmin repositoryAdmin;

    public synchronized void setRepositoryAdmin(final RepositoryAdmin repAdmin) {
        RepositoryAdminService.repositoryAdmin = repAdmin;
    }

    public static RepositoryAdmin getInstance() {
        return RepositoryAdminService.repositoryAdmin;

    }

    protected void activate(final ComponentContext component) {
        // http://felix.apache.org/site/apache-felix-osgi-bundle-repository.html
        ConfigService configService = ConfigServiceProvider.getConfigService();
        if (configService == null) {
            logger.warn("was not able to assign skysail config service, aborting adding OBR repositories");
        }
        String repositories = configService.getString("obrRepositories");
        if (repositories == null) {
            logger.info("no OBR repositories defined");
            return;
        }
        String[] splits = repositories.split(",");
        for (String repository : splits) {
            try {
                logger.info("trying to add repository '{}' to list of OBRs", repository);
                repositoryAdmin.addRepository(repository.trim());
            } catch (Exception e) {
                logger.error("was not able to add repository '{}' to list of OBR: {}", repository, e.getMessage());
            }
        }
    }

}
