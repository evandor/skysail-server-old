package de.twenty11.skysail.server.graphs.test;

import java.util.ArrayList;
import java.util.List;

import de.twenty11.skysail.common.graphs.EdgeProvider;
import de.twenty11.skysail.common.graphs.GraphProvider;
import de.twenty11.skysail.common.graphs.Graph;
import de.twenty11.skysail.common.graphs.NodeProvider;

@Graph(nodesPath="/osgimonitor/bundles")
public class GraphTestClass implements GraphProvider {

    private List<NodeProvider> nodes = new ArrayList<NodeProvider>();
    List<EdgeProvider> edges = new ArrayList<EdgeProvider>();

    public GraphTestClass() {
       
    }
    
    public List<NodeProvider> getNodes() {
        return nodes;
    }
    
    public List<EdgeProvider> getEdges() {
        return edges;
    }
    
}
