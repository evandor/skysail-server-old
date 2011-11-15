package de.twenty11.skysail.server.osgi.um.metadata

import de.twenty11.skysail.server.EntityService
import de.twenty11.skysail.server.osgi.um.users.UserComponentJava

class UserService {

    @scala.reflect.BeanProperty
  var service: EntityService = new UserComponentJava()
}