package de.skysail.server.restletosgi.test;

import org.restlet.resource.ClientResource;

import de.twenty11.skysail.server.restletosgi.internal.Activator;

public class WadlMain {

    public static void main(String[] args) throws Exception {
        ClientResource service = new ClientResource("http://localhost:"+Activator.HTTP_SERVICE_PORT+"/");
        System.out.println(service.options().getText());
    }
    
}
