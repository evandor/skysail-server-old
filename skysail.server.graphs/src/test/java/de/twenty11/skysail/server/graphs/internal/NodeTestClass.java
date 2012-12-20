package de.twenty11.skysail.server.graphs.internal;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonSubTypes;

import de.twenty11.skysail.common.graphs.NodeProvider;

@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = NodeTestClass.class, name = "de.twenty11.skysail.server.graphs.internal.NodeTestClass") })
public class NodeTestClass implements NodeProvider {

	String labelfield = "graphTestClassLabel";
    private String id;
    private String label;
    private String type;
    

	public NodeTestClass(String id, String label) {
	    this.id = id;
	    this.label = label;
    }
	
    @Override
    public String getNodeId() {
        return id;
    }

    @Override
    public String getNodeLabel() {
        return label;
    }

    @Override
    public Map<String, String> getNodeProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(id, label);
        return result;
    }

    @Override
    public String getType() {
       return this.getClass().getCanonicalName();
    }
   
	
}
