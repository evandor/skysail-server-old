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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkysailUtils {

	/**
	 * Utility method to create a representation of an entity model (typically the metadata
	 * for a grid structure
	 * 
	 * @param fields
	 * @return
	 */
	@Deprecated
	public static List<Map<String, Object>> createModelList(String[] fields) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < fields.length; i++) {
			Map<String, Object> entry = new HashMap<String, Object>();
			entry.put("header",fields[i]);
			entry.put("sortable",true);
            entry.put("editable", true);
			if (fields[i].equals("id"))
			    entry.put("editable", false);
			entry.put("index",fields[i]);
			entry.put("width","150");
			if (fields[i].equals("id"))
                entry.put("width", "50");
			result.add(entry);
		}
		return result;
	}
}
