package de.twenty11.skysail.server.freemarker;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.server.freemarker.internal.MapBasedUrlTemplateLoader;
import de.twenty11.skysail.server.freemarker.internal.TemplateInfo;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * The FtlTrackerCustomizer 'hooks in' when a bundle containing the
 * BUNDLE_TEMPLATE_HEADER is added, modified or removed.
 * 
 * original idea: see http://dani.calidos.com/2010/08/11/templating-the-osgi-way-with-freemarker/
 * 
 * @author carsten
 * 
 */
public class FtlTrackerCustomizer implements BundleTrackerCustomizer {

    /** time in seconds to check for new templates. */
    private static final int TEMPLATE_UPDATE_DELAY = 3600;

    /** slf4j based log. */
    private static Logger log = LoggerFactory.getLogger(FtlTrackerCustomizer.class);

    /**
     * a freemarker configuration; if not set, a default configurion will be
     * used.
     */
    private Configuration freemarkerConfig;

    /** Identifier in bundles. */
    private static final String BUNDLE_TEMPLATE_HEADER = "FtlTemplates";

    /** . */
    private Map<String, TemplateInfo> templates = new HashMap<String, TemplateInfo>();

    /**
     * default constructor, creates a new default Freemarker Configuration and
     * sets the template loader.
     */
    public FtlTrackerCustomizer() {
        // no configuration provided, create a default one
        Configuration config = new Configuration();
        config.setObjectWrapper(new DefaultObjectWrapper());
        config.setLocalizedLookup(false);
        config.setTemplateUpdateDelay(FtlTrackerCustomizer.TEMPLATE_UPDATE_DELAY);
        config.setTemplateLoader(new MapBasedUrlTemplateLoader(templates));
        // set template loader with the default configuration
        setFreemarkerConfiguration(config);
    }

    /**
     * @param conf
     *            potentially existing freemarker configuration (or null)
     */
    public FtlTrackerCustomizer(final Configuration conf) {
        setFreemarkerConfiguration(conf);
    }

    /**
     * whenever a bundle is added, its location is checked for a specific header
     * ("FtlTemplates") and, if existing, its value (a location) will be used to
     * retrieve all *.ftl files.
     * 
     * More specifically, the URLs to those ftl files will be saved together
     * with the containing bundle in the templates member.
     * 
     * @see org.osgi.util.tracker.BundleTrackerCustomizer#addingBundle(org.osgi.framework
     *      .Bundle, org.osgi.framework.BundleEvent)
     * @param bundle
     *            osgi bundle
     * @param event
     *            bundleEvent is ignored
     * @return null
     */
    @Override
    @SuppressWarnings("unchecked")
    public final Object addingBundle(final Bundle bundle, final BundleEvent event) {

        String locationInBundle = (String) bundle.getHeaders().get(BUNDLE_TEMPLATE_HEADER);
        if (locationInBundle == null) { // nothing found
            return null;
        }

        // Remark: The urls return from this method might look different when
        // using different OSGi frameworks
        Enumeration<URL> bundleTemplatesUrls = bundle.findEntries(locationInBundle, "*.ftl", true);
        addTemplates(bundle, locationInBundle, bundleTemplatesUrls);
        // add css files as well
        Enumeration<URL> bundleCssUrls = bundle.findEntries(locationInBundle, "*.css", true);
        addTemplates(bundle, locationInBundle, bundleCssUrls);
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.util.tracker.BundleTrackerCustomizer#modifiedBundle(org.osgi
     * .framework.Bundle, org.osgi.framework.BundleEvent, java.lang.Object)
     */
    @Override
    public final void modifiedBundle(final Bundle bundle, final BundleEvent event, final Object object) {
        removedBundle(bundle, event, object);
        addingBundle(bundle, event);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.osgi.util.tracker.BundleTrackerCustomizer#removedBundle(org.osgi.
     * framework.Bundle, org.osgi.framework.BundleEvent, java.lang.Object)
     */
    @Override
    // TODO test me
    public final void removedBundle(final Bundle bundle, final BundleEvent event, final Object object) {
        freemarkerConfig.clearTemplateCache();
    }

    /**
     * create identifiers and store the provided templateInfo with those names.
     * 
     * @param bundle
     *            the one containing the template
     * @param templateName
     *            e.g. 'description.ftl'
     * @param templateInfo
     *            the necessary information about where to find the template
     */
    private void addTemplateInfo(final Bundle bundle, final String templateName, final TemplateInfo templateInfo) {
        addTemplateWithIdentifier(templateInfo, getTemplateIdentifier(bundle, templateName));
        addTemplateWithIdentifier(templateInfo, getTemplateIdentifierWithVersion(bundle, templateName));
    }

    /**
     * Helper function to add a new templateInfo to the templates member.
     * 
     * @param newTemplate
     *            templateInfo
     * @param identifier
     *            identifier in the templates list
     */
    private void addTemplateWithIdentifier(final TemplateInfo newTemplate, final String identifier) {
        templates.put(identifier, newTemplate);
        log.debug("Added template #{}: '{}'", templates.size(), identifier);
    }

    /**
     * find the 'base' name (without leading slash).
     * 
     * @param bundleEntryUrl
     *            something like
     *            bundleentry://5.fwk357451187/freemarker/ftls/description.ftl
     * @param locationInBundle
     *            for example '/freemarker/ftls/'
     * @return normalized Name like 'description.ftl'
     */
    private String getTemplateName(final URL bundleEntryUrl, final String locationInBundle) {

        // gives something like '/freemarker/ftls/description.ftl'
        String templatePath = bundleEntryUrl.getPath();

        // 'remove' relative location in bundle and leading slash
        String name = templatePath.substring(locationInBundle.length());
        if (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;

    }

    /**
     * Create template URL id of the form '<bundle>:<relativePath>'.
     * 
     * @param bundle
     *            bundle with the template
     * @param templateName
     *            relative path to template
     * @return built Identifier of the form '<bundle>:<relativePath>'
     */
    private String getTemplateIdentifier(final Bundle bundle, final String templateName) {
        return bundle.getSymbolicName() + ":" + templateName;
    }

    /**
     * Create template URL id of the form
     * 'bundle://BUNDLENAME:BUNDLEVERSION/RELATIVEPATH'.
     * 
     * @param bundle
     *            containing the template, to extract the name
     * @param templateID
     *            relative path of the template
     * @return created URL
     */
    private String getTemplateIdentifierWithVersion(final Bundle bundle, final String templateID) {
        return bundle.getSymbolicName() + ":" + bundle.getVersion() + ":" + templateID;
    }

    /**
     * @return freemarker configuration being used by the tracker
     */
    public final Configuration getFreemarkerConfiguration() {
        return freemarkerConfig;
    }

    /**
     * @param conf
     *            the provided configuration, may not be null
     */
    private void setFreemarkerConfiguration(final Configuration conf) {

        // method's contract
        if (conf == null) {
            throw new IllegalArgumentException("provided configuration was null");
        }

        // setting the provided configuration
        freemarkerConfig = conf;

        TemplateLoader existingTemplateLoader = freemarkerConfig.getTemplateLoader();
        if (existingTemplateLoader != null) {
            freemarkerConfig.setTemplateLoader(new MapBasedUrlTemplateLoader(templates));

            TemplateLoader dynamicTemplateLoader = freemarkerConfig.getTemplateLoader();
            freemarkerConfig.setTemplateLoader(new MultiTemplateLoader(new TemplateLoader[]{existingTemplateLoader,
                            dynamicTemplateLoader}));

        } else {
            log.warn("Service freemarker Configuration had no template loader, using default bundle loader");
            freemarkerConfig.setTemplateLoader(new MapBasedUrlTemplateLoader(templates));
        }
    }

    /**
     * helper for adding the templates.
     * @param bundle containing the template
     * @param locationInBundle location inside the bundle
     * @param bundleTemplatesUrls 
     */
    private void addTemplates(final Bundle bundle, final String locationInBundle,
                    final Enumeration<URL> bundleTemplatesUrls) {
        if (bundleTemplatesUrls == null) {
            return;
        }
        log.info("Adding templates from bundle :{}", bundle.getSymbolicName());
        log.info("Relative location is '{}'", locationInBundle);
        while (bundleTemplatesUrls.hasMoreElements()) {
            URL bundleEntryUrl = bundleTemplatesUrls.nextElement();
            log.info(" >>> adding " + bundleEntryUrl);
            TemplateInfo newTemplate = new TemplateInfo(bundle, bundleEntryUrl, locationInBundle);
            String templateName = getTemplateName(bundleEntryUrl, locationInBundle);
            // createTemplateInfo(bundle, bundleEntryUrl, locationInBundle);
            addTemplateInfo(bundle, templateName, newTemplate);
        }
    }

}
