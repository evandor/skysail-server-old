package de.skysail.server;

import java.util.Map;


/**
 * Implementors provide a <String,String> mapping via 'getUrlMapping' meant to describe a path -> classname relation.
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
    Map<String, String> getUrlMapping();

}
