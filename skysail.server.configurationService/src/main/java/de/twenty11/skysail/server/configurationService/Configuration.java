package de.twenty11.skysail.server.configurationService;

import de.twenty11.skysail.server.service.definition.ConfigService;

public class Configuration implements ConfigService {

    @Override
    public String getString(String identifier) {
        return identifier;
    }

}
