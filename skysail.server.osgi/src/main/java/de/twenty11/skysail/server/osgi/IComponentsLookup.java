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

package de.twenty11.skysail.server.osgi;

import java.security.Principal;
import java.util.List;

import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.server.EntityService;

public interface IComponentsLookup {

    List<TreeNodeData> getComponents(Principal userPrincipal);

    EntityService getComponent(String name);
    
    Object getImage(String componentName, String identifier, String size);

}
