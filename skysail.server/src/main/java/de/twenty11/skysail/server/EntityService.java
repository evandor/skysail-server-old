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
package de.twenty11.skysail.server;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.common.messages.EntityData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.messages.GridInfo;
import de.twenty11.skysail.common.messages.TreeNodeData;


/**
 * An EntityService takes care of a specific entity, e.g. a note, and provides
 * basic CRUD operations on that entity. Furthermore, it is able to provide icons
 * related to this entity and all the information needed to display a grid (or table)
 * of a list of those entities or a form (not yet implemented). An EntityService also
 * provides a menu structure with commands to be applied on the entities.
 * 
 * @author carsten
 *
 */
public interface EntityService {
    
    /**
     * Create a new entity from its serialized form.

     * A representation of the created entity (for example enriched with an id 
     * in case a database entry is created) is returned.
     * 
     * If something goes wrong, this will throw an unchecked exception.
     * 
     * @param serializedEntity entity string like '{"data":{"title":"mytitle","note":"mynote"}}'
     * @return EntityData describing the created entity
     */
    EntityData createEntity (Principal userPrincipal, String serializedEntity);

	/**
     * Delete the entity with the provided id.
     * 
     * If something goes wrong, this will throw an unchecked exception.
     * 
	 * @param id of the entity to be deleted
	 */
	void deleteEntity(Long id);

    
    /**
     * @param cmdName
     * @param request
     * @return
     */
    GridData updateEntity (Principal userPrincipal, String serializedEntity);

	/**
     * Returns the map of child nodes for a given menu request.
     * 
     * Specific behavior is expected from implementors: 
     * 
     * TODO check
     * If menuElementIdentifier is null or empty, throw IllegalArgumentException.
     * If menuElementIdentifier equals "/", return a Map of keys with the names of 
     *   legal menu identifier names (the map values can be ignored)
     * 
     * This method takes a simple string as identifier, as it will typically be
     * invoked from a web request (which will not pass objects, but simple strings).
     * 
     * @param queryFromPath 
     * @return map between the name of the node and the treeNodeData defining the node
     */
    List<TreeNodeData> getSubMenu(String menuElementIdentifier);



	/**
	 * @param pathInfo
	 * @param parameterMap
	 * @return
	 */
	GridInfo getGridInfo(String pathInfo, Map<String,String[]> parameterMap);

	/**
	 * @param userPrincipal 
	 * @param pathInfo
	 * @param parameters
	 * @return
	 */
	GridData getGridData(Principal userPrincipal, String pathInfo, Map<String, String[]> parameters);

    Object runCommand(String cmdName, Map<String, String[]> parameterMap);

	/**
	 * last resort method for unforseen invocations
	 * @param commandName
	 * @param pathInfo
	 * @return
	 */
	Object execute(String commandName, String pathInfo);

	Object getImage(String identifier, String size);

	/**  === Permissions ============================================== */
	// not defined here (yet?)
	// void setPermissionService(IPermissionService service);
	
}
