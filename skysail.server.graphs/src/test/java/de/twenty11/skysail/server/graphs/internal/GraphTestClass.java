package de.twenty11.skysail.server.graphs.internal;

import java.util.ArrayList;
import java.util.List;

import de.twenty11.skysail.common.graphs.EdgeProvider;
import de.twenty11.skysail.common.graphs.GraphProvider;
import de.twenty11.skysail.common.graphs.Graph;
import de.twenty11.skysail.common.graphs.NodeProvider;

@Graph(nodesPath="dbviewer/graph")
public class GraphTestClass implements GraphProvider {

    private List<NodeProvider> nodes = new ArrayList<NodeProvider>();
    List<EdgeProvider> edges = new ArrayList<EdgeProvider>();

    public GraphTestClass() {
        nodes.add(new NodeTestClass("myid", "mylabel"));
        nodes.add(new NodeTestClass("yourid", "yourlabel"));
        edges.add(new EdgeTestClass("edgeid", "edgelabel", nodes.get(0), nodes.get(1), 12));
    }
    
    public List<NodeProvider> getNodes() {
        return nodes;
    }
    
    public List<EdgeProvider> getEdges() {
        return edges;
    }
    
}
