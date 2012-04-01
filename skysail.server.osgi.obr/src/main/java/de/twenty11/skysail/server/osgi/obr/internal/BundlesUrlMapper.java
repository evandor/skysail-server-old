package de.twenty11.skysail.server.osgi.obr.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import de.twenty11.skysail.server.osgi.obr.InstallResource;
import de.twenty11.skysail.server.osgi.obr.RepositoriesResource;
import de.twenty11.skysail.server.osgi.obr.RepositoryResource;
import de.twenty11.skysail.server.servicedefinitions.UrlMapper;

/**
 * Maps urls to resource handlers.
 * 
 * @author carsten
 * 
 */
public class BundlesUrlMapper implements UrlMapper {

    /* (non-Javadoc)
     * @see de.twenty11.skysail.server.servicedefinitions.UrlMapper#getUrlMapping()
     */
    public Map<String, String> getUrlMapping() {
        Map<String, String> queue = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        queue.put("/osgirep/repositories/", RepositoriesResource.class.getName());
        queue.put("/osgirep/repositories/{repositoryName}/", RepositoryResource.class.getName());
        queue.put("/osgirep/repositories/{repositoryName}/{bundle}/install/", InstallResource.class.getName());
        return queue;
    }

}
