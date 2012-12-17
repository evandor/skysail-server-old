package de.twenty11.skysail.server.graphs.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.graphs.GraphsResource;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

public class GraphsUrlMapper implements UrlMapper {

//    public static final String CONTEXT_ID = "dbviewer";
//
//    public static final String CONNECTION_PREFIX = "/" + CONTEXT_ID + "/connections/";
//
//    public static final String CONNECTION_NAME = "connectionName";
//
//    public static final String SCHEMA_NAME = "schema";
//
//    public static final String TABLE_NAME = "tableName";

    @Override
    public Map<String, String> provideUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        
        Map<ApplicationDescriptor, GraphsModel> formModels = GraphsSkysailApplication.get().getGraphModels();
        for (ApplicationDescriptor application : formModels.keySet()) {
            String rootName = application.getApplicationDescription().getName();
            queue.put("/" + rootName + "/graphs", GraphsResource.class.getName());
            
        }
        
        // @formatter:off
        // @formatter:on
        return queue;
    }

}
