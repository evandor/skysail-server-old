package de.twenty11.skysail.server.management;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class RestartResource extends ServerResource {

    /** slf4j based logger implementation */
    private static Logger logger = LoggerFactory.getLogger(RestartResource.class);
    private String resourceRef;

    /** deals with json objects */
    private final ObjectMapper mapper = new ObjectMapper();

    public RestartResource() {
        // setName("generic graph representation resource");
        // setDescription("The graph representation for the referenced resource");
    }

    @Override
    protected void doInit() throws ResourceException {
        // // TODO improve
        // resourceRef = getRequest().getResourceRef().getParentRef().toString();
        // // strip last slash
        // resourceRef = resourceRef.substring(0, resourceRef.length() - 1);
    }

    @Get
    public StringRepresentation getGraph() {
        // final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

        final ArrayList<String> command = new ArrayList<String>();
        command.add("/home/carsten/git/skysail-server-ext/skysail.server.ext.osgimonitor/etc/pax-runner/local.sh");

        final ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new StringRepresentation("problem restarting...");
        }
        System.exit(0);
        return new StringRepresentation("restarting...");

    }


}
