package de.twenty11.skysail.server.um.services;

import de.twenty11.skysail.server.um.domain.SkysailUser;

public interface UserManager {

    SkysailUser findByUsername(String username);

}
