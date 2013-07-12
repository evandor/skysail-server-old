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
package de.twenty11.skysail.server.um.resources;

import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailUser;

import java.util.List;

/**
 *
 * @author graefca
 */
public class UsersResource extends ListServerResource2<SkysailUser> {

    @Override
    protected List<SkysailUser> getData() {
        UserManagementApplication app = (UserManagementApplication)getApplication();
        return app.getUserRepository().getEntities();
    }
}
