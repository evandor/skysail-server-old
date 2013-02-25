package de.twenty11.skysail.server.services;

import org.restlet.Component;
import org.restlet.security.Verifier;

public interface ComponentProvider {

    Component getComponent();

    Verifier getVerifier();

}
