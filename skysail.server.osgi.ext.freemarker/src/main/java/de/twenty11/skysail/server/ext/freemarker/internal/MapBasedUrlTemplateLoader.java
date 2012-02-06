package de.twenty11.skysail.server.ext.freemarker.internal;

import java.net.URL;
import java.util.Map;

import freemarker.cache.URLTemplateLoader;

/**
 * This class is instantiated from the FtlTrackerCustomizer and
 * provides the actual Template URLs.
 * 
 * @author carsten
 * 
 */
public class MapBasedUrlTemplateLoader extends URLTemplateLoader {

    /** Map between an identifier and the associated TemplateInfo. */
    private Map<String, TemplateInfo> templates;

    /**
     * @param templateMap the templates
     */
    public MapBasedUrlTemplateLoader(final Map<String, TemplateInfo> templateMap) {
        this.templates = templateMap;
    }

    /**
     * @param identifier
     *            for example "de.evandor.skysail.server.osgi.ext.freemarker:description.ftl"
     * @see freemarker.cache.URLTemplateLoader#getURL(java.lang.String)
     * @return the url of the template 
     * (like bundleentry://252.fwk2092843500/freemarker/ftls/menu.ftl) or null if not found
     */
    @Override
    protected final URL getURL(final String identifier) {
        TemplateInfo templateInfo = templates.get(identifier);
        if (templateInfo != null) {
            return templateInfo.getTemplateURL();
        }
        return null;
    }
}
