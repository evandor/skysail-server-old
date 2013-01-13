package de.twenty11.skysail.server.graphs;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.graphs.EdgeDetails;
import de.twenty11.skysail.common.graphs.EdgeProvider;
import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.common.graphs.NodeDetails;
import de.twenty11.skysail.common.graphs.NodeProvider;
import de.twenty11.skysail.common.graphs.OutgoingEdgesProvider;
import de.twenty11.skysail.common.graphs.RestfulGraph;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.restlet.GenericServerResource;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;

/**
 * Restlet Resource class for handling Connections.
 * 
 * Provides a method to retrieve the existing connections and to add a new one.
 * 
 * The managed entity is of type {@link ConnectionDetails}, providing details (like jdbc url, username and password
 * about what is needed to actually connect to a datasource.
 * 
 */
public class GraphsResource extends GenericServerResource<GraphDetails> implements RestfulGraph {

    /** slf4j based logger implementation */
    private static Logger logger = LoggerFactory.getLogger(GraphsResource.class);
    private String resourceRef;

    /** deals with json objects */
    private final ObjectMapper mapper = new ObjectMapper();

    public GraphsResource() {
        setName("generic graph representation resource");
        setDescription("The graph representation for the referenced resource");
    }

    @Override
    protected void doInit() throws ResourceException {
        // TODO improve
        resourceRef = getRequest().getResourceRef().getParentRef().toString();
        // strip last slash
        resourceRef = resourceRef.substring(0, resourceRef.length() - 1);
    }

    @Override
    @Get
    public Response<GraphDetails> getGraph() {
        return getEntity(graphRepresentation());// , "graph representation");
    }

    @SuppressWarnings("unchecked")
    private GraphDetails graphRepresentation() {
        RestletOsgiApplication application = (RestletOsgiApplication) getApplication();
        String applicationName = application.getApplicationName();
        GraphDetails graphDetails = new GraphDetails("graph");

        resourceRef = "riap://host/osgimonitor/bundles";
        ClientResource columns = new ClientResource(resourceRef);
        getContext().getDefaultVerifier();
        getClientInfo().getPrincipals();
        getClientInfo().getUser();
        getChallengeRequests();
        columns.setChallengeResponse(getChallengeResponse());
        getConditions();
        getProxyChallengeRequests();
        getRequestAttributes();
        // columns.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "admin", "skysail"));
        Representation representation = columns.get();
        try {
            String representationAsText = representation.getText();
            createNodes(graphDetails, representationAsText);
            createEdges(graphDetails, representationAsText);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return graphDetails;

    }

    private void createNodes(GraphDetails graphDetails, String representationAsText) throws IOException,
            JsonParseException, JsonMappingException {
        Response<List<? extends NodeProvider>> response = mapper.readValue(representationAsText,
                new TypeReference<Response<List<? extends NodeProvider>>>() {
                });
        List<? extends NodeProvider> payload = response.getData();
        for (NodeProvider nodeProvider : payload) {
            NodeDetails node = new NodeDetails(nodeProvider.getNodeId(), nodeProvider.getNodeLabel());
            graphDetails.addNode(node);
        }
    }

    private void createEdges(GraphDetails graphDetails, String representationAsText) throws IOException,
            JsonParseException, JsonMappingException {
        Response<List<? extends OutgoingEdgesProvider>> response = mapper.readValue(representationAsText,
                new TypeReference<Response<List<? extends OutgoingEdgesProvider>>>() {
                });
        List<? extends OutgoingEdgesProvider> payload = response.getData();
        for (OutgoingEdgesProvider edgesProvider : payload) {
            for (EdgeProvider edgeProvider : edgesProvider.getEdges()) {
                EdgeDetails edge = new EdgeDetails(edgeProvider.getEdgeLabel(), edgeProvider.getSource(), edgeProvider.getTarget());
                graphDetails.addEdge(edge);
            }
        }
    }

}
