package de.twenty11.skysail.server.osgi.um.metadata

import java.util.ArrayList
import java.util.Collections
import java.util.HashMap
import java.util.List
import java.util.Map
import org.codehaus.jackson.map.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.codehaus.jackson.`type`.TypeReference
import org.springframework.transaction.annotation.Transactional
import scala.collection.JavaConversions._
import de.twenty11.skysail.common.messages.TreeNodeData
import de.twenty11.skysail.common.messages.EntityData
import de.twenty11.skysail.common.SkysailRequest
import de.twenty11.skysail.common.messages.GridInfo
import de.twenty11.skysail.server.osgi.SkysailUtils
import de.twenty11.skysail.common.messages.GridData
import de.twenty11.skysail.common.RowData
import de.twenty11.skysail.server.EntityService
import java.security.Principal
import de.twenty11.skysail.server.osgi.SkysailUtils
import de.twenty11.skysail.server.EntityService
import de.twenty11.skysail.server.osgi.um.metadata.MetadataDao
import de.twenty11.skysail.server.osgi.um.metadata.Metadata
import de.twenty11.skysail.server.osgi.SkysailUtils
import de.twenty11.skysail.server.EntityService

/**
 * Entry point to manage Notes. provides methods to create a note and to display
 * the current list of existing notes. To be extended.
 */
@Transactional
class MetadataComponent extends EntityService {

  val fields = Array("id")//, "parentId", "name")

  /** slf4j backed logger service */
  val logger = LoggerFactory.getLogger(this.getClass())

  /**
   * injected dao
   */
  @scala.reflect.BeanProperty
  var dao: MetadataDao = new MetadataDao() //Option[MetadataDao] = None

  def getSubMenu(queryFromPath: String): List[TreeNodeData] = {
    Collections.emptyList()
  }

  def createEntity(user: Principal, serializedEntity: String): EntityData = {
    val mapper = new ObjectMapper();
    //val entity = new JacksonReader[Metadata]().read(serializedEntity);
	val request:SkysailRequest[Metadata] = mapper.readValue(serializedEntity, 
		new TypeReference[SkysailRequest[Metadata]](){});
	val entity = request.getData()
    dao.save(entity)
    returnResult(entity)
  }

  def deleteEntity(id: java.lang.Long): Unit = {
    dao.delete(dao.get(id))
  }

  def runCommand(cmdName: String, parameters: java.util.Map[String, Array[String]]): Object = {
    logger.info("running command '" + cmdName + "'");
    val result = new ArrayList[Map[String, Object]]();
    //  		if (cmdName.equals("fields")) {
    SkysailUtils.createFieldList(fields);
    //  		} else (cmdName.equals("show")) {
    //  			showData(result);
    //  		}
  }

  def updateEntity(user: Principal, serializedEntity: String): GridData = {
    val mapper = new ObjectMapper();
    //  		var userData: Map
    //  		try {
    val userData = mapper.readValue(serializedEntity, classOf[Map[String, Object]])
    val map = userData.get("data").asInstanceOf[Map[String, Object]]
    val sb = new StringBuffer("{");
    for (key <- map.keySet()) {
      sb.append('"').append(key).append("\" : \"")
        .append(map.get(key)).append("\",");
    }
    val readValue = mapper.readValue(
      sb.toString().substring(0, sb.length() - 1) + "}",
      classOf[Metadata]);
    dao.update(readValue);

    returnResult(map);
    //  		} catch (Exception e) {
    //  			logger.error("Error creating entity", e);
    //  			return null;
    //  		}
  }

  def getGridData(user: Principal, pathInfo: String, parameters: java.util.Map[String, Array[String]]): GridData = {
  		val entities = dao.list();
  		val fieldsList = SkysailUtils.createFieldList(fields);
  		val grid = new GridData(fieldsList.getColumns());
  		for (entity <- entities) {
  		  
  			val col = new RowData();
  			val cols = new ArrayList[Object]();
  			cols.add(entity.getId().asInstanceOf[Object]);
  			//cols.add(entity.getParentId.asInstanceOf[Object]);
  			//cols.add(entity.getName.asInstanceOf[Object]);
  			col.setColumnData(cols);
  			grid.addRowData(col);
  		}
  		return grid;
  	}
  //
  def returnResult(map: Map[String, Object]): GridData = {

    val fieldsList = SkysailUtils.createFieldList(fields);
    val result = new GridData(fieldsList.getColumns());
    val rowData = new RowData();
    rowData.setColumnData(Collections.list(Collections.enumeration(map
      .values())));
    // result.add(map);
    //result.addRowData(rowData)
    result;
  }

  def returnResult(entity: Metadata): EntityData = {
    val mapper = new ObjectMapper();
    val result = new EntityData();

    // TODO improve
    val fields = new HashMap[String, Object]();
    fields.put("id", entity.getId().asInstanceOf[Object]);
    //fields.put("parentId", entity.getParentId().asInstanceOf[Object]);
    //fields.put("name", entity.getName().asInstanceOf[Object]);
    result.setFields(fields);
    return result;
  }
  
  	def execute (command: String, path: String): Object = {
	  None
	}
  	
  	def getGridInfo (pathInfo: String, parameters: java.util.Map[String, Array[String]]): GridInfo = {
  	  SkysailUtils.createFieldList(fields);
  	}

  	def getImage(identifier: String, size: String): Object = {
	  this.getClass().getResourceAsStream("/img/16x16/metadatas.gif");
  	}

}
