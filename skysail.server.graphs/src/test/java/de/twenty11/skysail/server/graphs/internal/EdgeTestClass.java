package de.twenty11.skysail.server.graphs.internal;

import java.util.HashMap;
import java.util.Map;

import de.twenty11.skysail.common.graphs.EdgeProvider;
import de.twenty11.skysail.common.graphs.NodeProvider;

public class EdgeTestClass implements EdgeProvider {

	String labelfield = "graphTestClassLabel";
    private String id;
    private String label;
    private NodeProvider source;
    private NodeProvider target;
    private int weight;

	public EdgeTestClass(String id, String label, NodeProvider source, NodeProvider target, int weight) {
	    this.id = id;
	    this.label = label;
	    this.source = source;
	    this.target = target;
	    this.weight = weight;
    }
	
    @Override
    public String getEdgeId() {
        return id;
    }

    @Override
    public String getEdgeLabel() {
        return label;
    }

    @Override
    public Map<String, String> getEdgeProperties() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(id, label);
        return result;
    }

    @Override
    public String source() {
        return source.getNodeId();
    }

    @Override
    public String target() {
        return target.getNodeId();
    }

    @Override
    public int weight() {
        return weight;
    }
	
}
