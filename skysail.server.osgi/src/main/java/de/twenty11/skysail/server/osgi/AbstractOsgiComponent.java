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
import java.util.Map;

import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.server.EntityService;

public abstract class AbstractOsgiComponent implements EntityService {

	abstract public List<TreeNodeData> getComponentSubMenu(String menuElementIdentifier);

	/* (non-Javadoc)
	 * @see de.twenty11.skysail.server.osgi.OsgiComponent#getSubMenu(java.lang.String)
	 */
	@Override
	public List<TreeNodeData> getSubMenu(String menuElementIdentifier) {
		// basic checks
		if (menuElementIdentifier == null)
			throw new IllegalArgumentException();
		if (menuElementIdentifier.length() == 0)
			throw new IllegalArgumentException();
		if (menuElementIdentifier.equals("/"))
			return null;//getRootMenusOld();
		// get sub menue from component
		return getComponentSubMenu(menuElementIdentifier);
	}

	@Override
	public Object runCommand(String cmdName, Map<String, String[]> parameterMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridData updateEntity(Principal user, String str) {
		// TODO Auto-generated method stub
		return null;
	}

}
