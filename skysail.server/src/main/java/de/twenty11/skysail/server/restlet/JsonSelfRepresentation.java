package de.twenty11.skysail.server.restlet;

import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;

public class JsonSelfRepresentation extends JsonRepresentation {

    public JsonSelfRepresentation(ApplicationInfo applicationInfo) {
        super(applicationInfo);
    }
    
    


}
