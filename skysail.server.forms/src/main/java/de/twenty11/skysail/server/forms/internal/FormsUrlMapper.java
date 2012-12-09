package de.twenty11.skysail.server.forms.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.forms.FormsResource;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

public class FormsUrlMapper implements UrlMapper {

    public static final String CONTEXT_ID = "dbviewer";

    public static final String CONNECTION_PREFIX = "/" + CONTEXT_ID + "/connections/";

    public static final String CONNECTION_NAME = "connectionName";

    public static final String SCHEMA_NAME = "schema";

    public static final String TABLE_NAME = "tableName";

    @Override
    public Map<String, String> provideUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        
        Map<ApplicationDescriptor, FormsModel> formModels = SkysailApplication.get().getFormModels();
        for (ApplicationDescriptor application : formModels.keySet()) {
            String rootName = application.getApplicationDescription().getName();
            queue.put("/" + rootName + "/forms", FormsResource.class.getName());
            
        }
        
        // @formatter:off

//        queue.put(CONNECTION_PREFIX, ConnectionsResource.class.getName());
//
//        String connection = CONNECTION_PREFIX + "{" + CONNECTION_NAME + "}";
//        queue.put(connection, ConnectionResource.class.getName());
//
//        String schemas = connection + "/schemas";
//        queue.put(schemas, SchemasResource.class.getName());
//
//        String schema = schemas + "/{schema}";
//
//        queue.put(schema + "/tables", TablesResource.class.getName());
//        queue.put(schema + "/tables/{" + TABLE_NAME + "}/columns",ColumnsResource.class.getName());
//        queue.put(schema + "/tables/{" + TABLE_NAME + "}/constraints", ConstraintsResource.class.getName());
//        queue.put(schema + "/tables/{" + TABLE_NAME + "}/data", DataResource.class.getName());
        
        // @formatter:on
        return queue;
    }

}
