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
package de.twenty11.skysail.server.um.init.db;

import java.net.URL;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.exceptions.DatabaseException;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author graefca
 */
public class Importer implements SessionCustomizer {

    private static Logger logger = LoggerFactory.getLogger(Importer.class);

    @Override
    public void customize(Session session) throws Exception {
        session.getEventManager().addListener(new SessionEventAdapter() {
            @Override
            public void postLogin(SessionEvent event) {
                String fileName = (String) event.getSession().getProperty("import.sql.file");
                UnitOfWork unitOfWork = event.getSession().acquireUnitOfWork();
                importSql(unitOfWork, fileName);
                unitOfWork.commit();
            }
        });
    }

    private void importSql(UnitOfWork unitOfWork, String filename) {
        URL importSqlUrl = this.getClass().getResource(filename);
        try {
            Scanner scanner = new Scanner(importSqlUrl.openStream());
            scanner.useDelimiter(";");
            logger.info("Initializing the database");
            while (scanner.hasNext()) {
                String sql = scanner.next();
                if (sql.trim().length() == 0) {
                    continue;
                }
                logger.info(" >>> running statement '{}'", sql);
                unitOfWork.executeNonSelectingSQL(sql);
            }
        } catch (DatabaseException dbe) {
            if (dbe.getCause() instanceof SQLIntegrityConstraintViolationException) {
                logger.debug("Ignoring initialization statement as it has been executed before: {} ", dbe.getCause()
                        .getMessage());
            } else {
                logger.error("Problem initializing the database", dbe);
            }
        } catch (Exception ex) {
            logger.error("Problem initializing the database", ex);
        }
    }
}
