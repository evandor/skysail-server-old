package de.twenty11.skysail.server.osgi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.twenty11.skysail.common.messages.GridInfo;
import de.twenty11.skysail.common.ColumnInfo;

public class SkysailUtils {

	public static GridInfo createFieldList (String[] fields) {

		List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
		for (int i=0; i < fields.length; i++) {
			ColumnInfo col = new ColumnInfo();
			col.setColumnName(fields[i]);
			columns.add(col);
			if (fields[i].equals("id"))
			    col.setEditable(false);
			if (fields[i].equals("id"))
                col.setWidth(50);
		}
		GridInfo gridInfo = new GridInfo();
		gridInfo.setColumns(columns);
		return gridInfo;
	}

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
