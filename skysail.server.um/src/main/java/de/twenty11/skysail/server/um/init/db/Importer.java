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

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import org.eclipse.persistence.sessions.UnitOfWork;

/**
 *
 * @author graefca
 */
public class Importer implements SessionCustomizer {

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
            scanner.useDelimiter("\\n");
            while (scanner.hasNext()) {
                unitOfWork.executeNonSelectingSQL(scanner.next());
            }
        } catch (IOException ex) {
            Logger.getLogger(Importer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
