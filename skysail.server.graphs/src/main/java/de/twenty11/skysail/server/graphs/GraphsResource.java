package de.twenty11.skysail.server.graphs;

import java.util.Collections;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.data.ChallengeRequest;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.common.forms.RestfulForms;
import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.common.graphs.NodeDetails;
import de.twenty11.skysail.common.graphs.NodeProvider;
import de.twenty11.skysail.common.graphs.RestfulGraph;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.server.restlet.GenericServerResource;
import de.twenty11.skysail.server.restlet.ListServerResource;
import de.twenty11.skysail.server.restlet.RestletOsgiApplication;

/**
 * Restlet Resource class for handling Connections.
 * 
 * Provides a method to retrieve the existing connections and to add a new one.
 * 
 * The managed entity is of type {@link ConnectionDetails}, providing details (like jdbc url, username
 * and password about what is needed to actually connect to a datasource.
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
        resourceRef = getRequest().getResourceRef().getParentRef().toString();
        // strip last slash
        resourceRef = resourceRef.substring(0, resourceRef.length()-1);
    }

    @Override
    @Get
    public Response<GraphDetails> getGraph() {
        return getEntity(graphRepresentation());//, "graph representation");
    }

    @SuppressWarnings("unchecked")
    private GraphDetails graphRepresentation() {
        RestletOsgiApplication application = (RestletOsgiApplication)getApplication();
        String applicationName = application.getApplicationName();
        GraphDetails graphDetails = new GraphDetails("graph");
        
        ClientResource columns = new ClientResource(resourceRef);
        columns.setChallengeResponse(new ChallengeResponse(ChallengeScheme.HTTP_BASIC, "scott", "tiger"));
        Representation representation = columns.get();
        try {
            Response<List<? extends NodeProvider>> response = mapper.readValue(representation.getText(),
                    new TypeReference<Response<List<? extends NodeProvider>>>() {
                    });
            List<? extends NodeProvider> payload = response.getData();
            for (NodeProvider nodeProvider : payload) {
                NodeDetails node = new NodeDetails(nodeProvider.getNodeId(), nodeProvider.getNodeLabel());
                graphDetails.addNode(node);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
   //     Map<ApplicationDescriptor, GraphsModel> graphModels = Activator.getGraphModels();
//        for (ApplicationDescriptor appDescriptor : formModels.keySet()) {
//            if (appDescriptor.getApplicationDescription().getName().equals(applicationName)) {
//                GraphsModel formsModel = formModels.get(appDescriptor);
//                return formsModel.getAllForms();
//            }
//        }
        return graphDetails;
        
    }

}
