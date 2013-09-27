package de.twenty11.skysail.server.core.restlet.testentities;

import javax.validation.constraints.NotNull;

public class SimpleEntity {

    private String name;

    public SimpleEntity() {
        // for tests
    }

    public SimpleEntity(String string) {
        this.name = string;
    }

    @NotNull(message = "Name is mandatory!")
    public String getName() {
        return name;
    }

}
