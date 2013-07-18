/*
 * Copyright 2013 GRAEFCA.
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
package de.twenty11.skysail.server.um.integration;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.ResourceTestWithUnguardedAppication;
import de.twenty11.skysail.server.internal.SkysailComponent;
import de.twenty11.skysail.server.um.UserManagementApplication;

import static org.hamcrest.Matchers.containsString;

import static org.junit.Assert.assertThat;

public class TestBase extends ResourceTestWithUnguardedAppication<UserManagementApplication> {

    private UserManagementApplication application;

    private static Component component = new SkysailComponent();

    @BeforeClass
    public static void init() throws Exception {
        component.getServers().add(Protocol.HTTP, TEST_PORT);
        component.start();
    }

    @AfterClass
    public static void stop() throws Exception {
        component.stop();
    }

    @Before
    public void setUp() {
        application = (UserManagementApplication) setUpApplication(new UserManagementApplication());
        application.setEntityManager(getEmfForTests());
        component.getDefaultHost().attach(application);
    }

    @Test
    @Ignore
    // FIXME
    public void request_for_users_resource_succeeds() throws Exception {
        ClientResource cr = new ClientResource(requestUrlFor("/users"));
        String json = cr.get().getText();
        assertThat(json, containsString("\"success\":true"));
    }

    @Test
    @Ignore
    // FIXME
    public void added_user_is_found_again() throws Exception {
        ClientResource cr = new ClientResource(requestUrlFor("/users/"));
        String json = cr.post(null).getText();
        assertThat(json, containsString("\"success\":true"));
    }
}
