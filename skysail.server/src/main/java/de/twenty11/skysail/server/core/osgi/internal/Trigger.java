package de.twenty11.skysail.server.core.osgi.internal;

public enum Trigger {
    // @formatter:off
    ATTACH ("attaching a application to a restlet component");
    // @formatter:on

    private String desc;

    Trigger(String desc) {
        this.desc = desc;
    }
}
