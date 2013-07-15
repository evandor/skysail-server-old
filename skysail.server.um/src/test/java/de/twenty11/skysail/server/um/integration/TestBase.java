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

import de.twenty11.skysail.server.um.resources.UsersResource;

import org.junit.Test;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.routing.Router;

public class TestBase {

    @Test
    public void test_server() throws Exception {

        final Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8182);
        final Router router = new Router(component.getContext().createChildContext());
        router.attach("/users", UsersResource.class);
        // Attach the sample application.
        component.getDefaultHost().attach("/restlet", router);
        // Start the component.
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    component.start();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        t.run();

        final ClientResource cr = new ClientResource("http://localhost:8182/restlet/users");
        Representation get = cr.get();
        //final UsersResource resource = cr.wrap(UsersResource.class);
        //final Person originalPerson = new Person("Peter", 20);
        //final Person copiedPerson = resource.copy(originalPerson);

        //Assert.assertFalse(originalPerson == copiedPerson);

        //Assert.assertTrue(originalPerson.name.equals(copiedPerson.name));

        System.out.println(get.getText());

    }
}
