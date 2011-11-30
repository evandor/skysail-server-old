package de.twenty11.skysail.server.osgi.bundles;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.filters.Filter;
import de.twenty11.skysail.common.messages.GridData;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;
import de.twenty11.skysail.server.osgi.bundles.internal.BundlesUrlMapper;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class BundlesResource extends SkysailServerResource<GridData> {

    /** slf4j based logger implementation */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundlesResource() {
        super("Bundles Representation");
        setTemplate("skysail.server.osgi.bundles:bundles.ftl");
    }

    @Override
    public GridData getData() {
        Set<String> queryNames = getQuery().getNames();
        Filter filter = new Filter();
        for (String queryName : queryNames) {
            filter.addFilter(queryName, getQuery().getValues(queryName));
        }
        return Bundles.getInstance().getBundles(filter);
    }

}
