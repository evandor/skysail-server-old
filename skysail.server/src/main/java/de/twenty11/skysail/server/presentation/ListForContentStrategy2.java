package de.twenty11.skysail.server.presentation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.Parameter;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.core.restlet.ListServerResource2;
import de.twenty11.skysail.server.presentation.stringtemplate.FormRenderer;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.utils.IOUtils;

public class ListForContentStrategy2 extends AbstractHtmlCreatingStrategy {

    private static Logger logger = LoggerFactory.getLogger(ListForContentStrategy2.class);

    private STGroupString template;

    private Parameter renderAs;

    private final ObjectMapper mapper = new ObjectMapper();

    public ListForContentStrategy2(BundleContext bundleContext, Resource resource) {
        SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
        renderAs = resource.getQuery().getFirst("renderAs");

        // First: classname in apps bundle
        template = searchIn(currentApplication.getBundle(), resource.getClass());
        if (template != null) {
            return;
        }

        // name of superclass in apps Bundle?
        template = searchIn(currentApplication.getBundle(), resource.getClass().getSuperclass());
        if (template != null) {
            return;
        }

        Bundle skysailServerBundle = getBundle(bundleContext, "skysail.server");

        template = searchIn(skysailServerBundle, resource.getClass().getSuperclass());
        if (template != null) {
            return;
        }

        template = searchIn(skysailServerBundle, ListServerResource2.class, false);
    }

    private STGroupString searchIn(Bundle bundle, Class<?> cls) {
        return searchIn(bundle, cls, true);
    }

    private STGroupString searchIn(Bundle bundle, Class<?> cls, boolean considerRenderAsParam) {
        if (bundle == null) {
            return null;
        }
        String location = determineLocation(cls, considerRenderAsParam);
        logger.info("Trying location {} in Bundle {}", location, bundle.getSymbolicName());
        return findTemplate(bundle, location);
    }

    private Bundle getBundle(BundleContext bundleContext, String symbolicName) {
        Bundle[] allBundles = bundleContext.getBundles();
        for (Bundle bundle : allBundles) {
            if (bundle.getSymbolicName().equals("skysail.server")) {
                return bundle;
            }
        }
        return null;
    }

    private String determineLocation(Class<?> cls, boolean considerRenderAsParameter) {
        String location = cls.getSimpleName() + ".stg";
        if (considerRenderAsParameter && renderAs != null) {
            location = renderAs.getValue() + "/" + location;
        }
        return location;
    }

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        List<?> data;
        if (skysailResponseAsObject instanceof List) {
            data = (List<?>) skysailResponseAsObject;
        } else {
            data = Arrays.asList(skysailResponseAsObject);
        }
        List<Object> result = new ArrayList<Object>();
        if (data != null) {
            for (Object object : data) {
                Map<String, Object> objectMap = mapper.convertValue(object, Map.class);
                objectMap.put("toString", object.toString());
                result.add(objectMap);
            }
        }
        String templateIdentifier = "accordion";
        if (renderAs != null) {
            templateIdentifier = renderAs.getValue();
        }

        template.registerRenderer(String.class, new FormRenderer());

        ST accordionHtml = template.getInstanceOf(templateIdentifier);
        accordionHtml.add("list", result);
        page = page.replace("${content}", accordionHtml.render());

        return page;
    }

    private STGroupString findTemplate(Bundle bundle, String templateSourceFile) {
        URL bundleResourceUrl = bundle.getResource("templates/" + templateSourceFile);
        if (bundleResourceUrl == null) {
            return null;
        }
        InputStream is;
        try {
            is = new BufferedInputStream(bundleResourceUrl.openStream());
            STGroupString result = new STGroupString(templateSourceFile, IOUtils.convertStreamToString(is), '$', '$');
            is.close();
            return result;
        } catch (IOException e) {
            logger.error("Problem reading bundle resource '{}'", templateSourceFile);
            throw new RuntimeException(e);
        }
    }

}
