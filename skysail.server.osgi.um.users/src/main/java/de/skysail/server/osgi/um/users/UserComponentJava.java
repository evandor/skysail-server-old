package de.skysail.server.osgi.um.users;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import de.twenty11.skysail.common.RowData;
import de.twenty11.skysail.common.SkysailRequest;
import de.twenty11.skysail.common.messages.EntityData;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.common.messages.GridInfo;
import de.twenty11.skysail.common.messages.TreeNodeData;
import de.twenty11.skysail.server.EntityService;
import de.twenty11.skysail.server.osgi.SkysailUtils;
import de.skysail.server.osgi.um.users.internal.UserDao;

/**
 * Entry point to manage Users. 
 * 
 * provides methods to create a user and to display the current list of existing  users. To be extended.
 */
@Transactional
public class UserComponentJava implements EntityService {

	private static final String[] fields = {"id", "login", "firstname", "lastname"};
	
	/** slf4j backed logger service */
	private static Logger logger = LoggerFactory.getLogger(UserComponentJava.class);

	/**
	 * injected dao to handle notes
	 */
	private UserDao dao;
	
	public void setUserDao(UserDao userDao) {
        this.dao = userDao;
    }
	
	/**
	 * Method getSubMenu.
	 * @param queryFromPath String
	 * @return Map<String,TreeNodeData>
	 * @see de.twenty11.skysail.server.EntityService#getSubMenu(String)
	 */
	@Override
	public List<TreeNodeData> getSubMenu(String queryFromPath) {
		return Collections.emptyList();
	}

	/**
	 * creates new entity based on data provided in request.
	 * 
	 * The data is supposed to be found in the requests 'entity' attribute.
	 * 
	 * @param request
	 * 
	 * @return List<Map<String,Object>>
	 */
	public EntityData createEntity(Principal user, String serializedEntity) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			SkysailRequest<User> request = mapper.readValue(serializedEntity, 
					new TypeReference<SkysailRequest<User>>(){});
			User newEntity = request.getData();
			dao.save(newEntity);
			return returnResult(newEntity);
		} catch (Exception e) {
			logger.error("Error creating entity", e);
			return null;
		}
	}

	@Override
	public void deleteEntity(Long id) {
		dao.delete(dao.get(id));
	}

	/**
	 * Method runCommand.
	 * @param cmdName String
	 * @return List<Map<String,Object>>
	 * @see de.twenty11.skysail.server.EntityService#runCommand(String)
	 */
	@Override
	public Object runCommand(String cmdName, Map<String, String[]> parameterMap) {
		logger.info("running command '" + cmdName + "'");
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>(); 
		if (cmdName.equals("model")) {
			return null;//skysailUtils.createModelList(fields);
		}
		else if (cmdName.equals("show")) {
			showData(result);
		}
		return null;//result;
	}

	/**
	 * Method execute.
	 * @param cmdName String
	 * @param request HttpServletRequest
	 * @return List<Map<String,Object>>
	 * @see de.twenty11.skysail.server.EntityService#execute(String, HttpServletRequest)
	 */
	@Override
	public GridData updateEntity(Principal user, String serializedEntity) {
		throw new NoSuchMethodError("could not execute command '' as no mapping exists in " + this.getClass().getName());
	}

	private List<Map<String, Object>> updateEntity(HttpServletRequest request) {
		String serializedEntity = (String)request.getAttribute("entity");
		ObjectMapper mapper = new ObjectMapper();
		Map userData;
		try {
			userData = mapper.readValue(serializedEntity, Map.class);
			Map map = (Map)userData.get("data");
		    StringBuffer sb = new StringBuffer("{");
		    for (Object key : map.keySet()) {
		        sb.append('"').append(key).append("\" : \"").append(map.get(key)).append("\",");
		    }
		    User readValue = mapper.readValue(sb.toString().substring(0,sb.length()-1) + "}", User.class);
			dao.update(readValue);
			
			ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			result.add(map);
			return result;
		} catch (Exception e) {
			logger.error("Error creating entity", e);
			return null;
		}
	}

	private List<Map<String, Object>> deleteEntity(HttpServletRequest request) {
		String serializedEntity = (String)request.getAttribute("entity");
		ObjectMapper mapper = new ObjectMapper();
		Map userData;
		try {
			userData = mapper.readValue(serializedEntity, Map.class);
			Map map = (Map)userData.get("data");
		    StringBuffer sb = new StringBuffer("{");
		    for (Object key : map.keySet()) {
		        sb.append('"').append(key).append("\" : \"").append(map.get(key)).append("\",");
		    }
		    User readValue = mapper.readValue(sb.toString().substring(0,sb.length()-1) + "}", User.class);
			dao.delete(readValue);
			
			ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			result.add(map);
			return result;
		} catch (Exception e) {
			logger.error("Error creating entity", e);
			return null;
		}
	}

	/**
	 * Method showData.
	 * @param result List<Map<String,Object>>
	 */
	private void showData(List<Map<String, Object>> result) {
		List<User> notes = dao.list();
		for (User note : notes) {
			Map<String, Object> resultEntry = new HashMap<String, Object>();
			resultEntry.put("id", note.getId());
//			resultEntry.put("title", note.getTitle());
//			resultEntry.put("note", note.getNote());
			result.add(resultEntry);
		}
	}

    @Override
    public GridData getGridData(Principal user, String pathInfo, Map<String,String[]> parameters) {
        
//        Role notesUserRole = permissionService.assertHasRole(user, "NotesUser");
//        permissionService.assertAccessGranted(user, notesUserRole, "getGridData");
        
        List<User> notes = dao.list();

        GridInfo fieldsList = SkysailUtils.createFieldList(fields);
        GridData grid = new GridData(fieldsList.getColumns());
        for (User note : notes) {
            RowData col = new RowData();
            List<Object> cols = new ArrayList<Object>();
            cols.add(note.getId());
            cols.add(note.getLogin());
            cols.add(note.getFirstname());
            cols.add(note.getLastname());
            col.setColumnData(cols);
            grid.addRowData(col);
        }
        return grid;
    }
	
	private EntityData returnResult(User entity) {
		ObjectMapper mapper = new ObjectMapper();
		EntityData result = new EntityData();
		
		// TODO improve
		Map<String, Object> entityfields = new HashMap<String,Object>();
		entityfields.put ("id", entity.getId());
		entityfields.put ("firstname", entity.getFirstname());
		entityfields.put ("lastname", entity.getLastname());
		entityfields.put ("loging", entity.getLogin());
		result.setFields(entityfields);
		return result;
	}

	@Override
	public Object execute(String commandName, String pathInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GridInfo getGridInfo(String pathInfo, Map<String,String[]> paramters) {
		return SkysailUtils.createFieldList(fields);
	}

	@Override
	public Object getImage(String identifier, String size) {
		return this.getClass().getResourceAsStream("/img/16x16/users.gif");
	}

}
