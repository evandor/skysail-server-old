package de.twenty11.skysail.server.core.osgi.internal;

public enum ApplicationState {
    // @formatter:off
    NEW      ("application has just been added to skysail"), 
    ATTACHED ("application has been attached to restlet component");
    // @formatter:on

    private String desc;

    ApplicationState(String desc) {
        this.desc = desc;
    }
}
