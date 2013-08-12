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

import java.util.List;

import org.restlet.data.Form;

import de.twenty11.skysail.common.Presentation;
import de.twenty11.skysail.common.PresentationStyle;
import de.twenty11.skysail.common.navigation.LinkedPage;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.um.UserManagementApplication;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Presentation(preferred = PresentationStyle.LIST2)
public class UsersResource extends ListServerResource2<SkysailUser> {

    @Override
    public SkysailResponse<List<SkysailUser>> getEntities() {
        return super.getEntities("Skysail System Users:");
    }

    @Override
    protected List<SkysailUser> getData() {
        UserManagementApplication app = (UserManagementApplication) getApplication();
        registerLinkedPage(new LinkedPage() {

            @Override
            public String getLinkText() {
                return "add user";
            }

            @Override
            public String getHref() {
                return "users/";
            }

            @Override
            public boolean applicable() {
                return true;
            }
        });
        return app.getUserRepository().getEntities();
    }

    @Override
    public SkysailUser getData(Form form) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SkysailResponse<?> addEntity(SkysailUser entity) {
        // TODO Auto-generated method stub
        return null;
    }
}
