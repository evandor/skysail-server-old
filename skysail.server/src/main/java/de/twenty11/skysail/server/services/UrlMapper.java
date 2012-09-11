/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */
package de.twenty11.skysail.server.services;

import java.util.Map;


/**
 * Implementors provide a <String,String> mapping via 'provideUrlMapping' meant to describe a path -> classname relation.
 * When a request for a specific path is received, the associated class is meant to "take care" of the request 
 *
 * @author carsten
 *
 */
public interface UrlMapper {

    /**
     * The implementor provides a list of url mappings between paths and classes to be executed
     * when such a path is being called.
     * 
     * The first String is a url restlet url mapping like 'notes/{command}' or the like,
     * the second corresponds to a class name.
     * 
     * Ordering might be important, in that case make sure to provide for example a
     * LinkedHashMap like this:
     * 
     * Set s = Collections.synchronizedMap(new LinkedHashMap(...))
     *
     * @return a map of path / classname pairs
     */
    Map<String, String> provideUrlMapping();

}
