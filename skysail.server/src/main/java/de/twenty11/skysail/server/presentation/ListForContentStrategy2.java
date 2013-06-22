package de.twenty11.skysail.server.presentation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.data.Parameter;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.presentation.render.DefaultCleaningStrategy;
import de.twenty11.skysail.server.presentation.render.HtmlRenderer;
import de.twenty11.skysail.server.presentation.render.MapTransformer;
import de.twenty11.skysail.server.restlet.SkysailApplication;
import de.twenty11.skysail.server.utils.IOUtils;

public class ListForContentStrategy2 extends AbstractHtmlCreatingStrategy {

    private static Logger logger = LoggerFactory.getLogger(ListForContentStrategy2.class);

    private STGroupString template;

    private Parameter renderAs;

    public ListForContentStrategy2(BundleContext bundleContext, Resource resource) {
        SkysailApplication currentApplication = (SkysailApplication) resource.getApplication();
        Bundle currentBundle = currentApplication.getBundle();
        renderAs = resource.getQuery().getFirst("renderAs");

        String location = resource.getClass().getSimpleName() + ".stg";
        if (renderAs != null) {
            location = renderAs.getValue() + "/" + location;
        }
        template = findTemplate(currentBundle, location);
        if (template == null) { // default template
            location = resource.getClass().getSuperclass().getSimpleName() + ".stg";
            if (renderAs != null) {
                location = renderAs.getValue() + "/" + location;
            }
            template = findTemplate(bundleContext.getBundle(), location);
            if (template == null) {
                location = resource.getClass().getSuperclass().getSimpleName() + ".stg";
                template = findTemplate(bundleContext.getBundle(), location);
            }
        }
    }

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            List<Object> result = new ArrayList<Object>();
            if (data != null) {
                for (Object object : data) {
                    result.add(new BeanMap(object));
                }
            }
            String templateIdentifier = "accordion";
            if (renderAs != null) {
                templateIdentifier = renderAs.getValue();
            }
            ST accordionHtml = template.getInstanceOf(templateIdentifier);
            accordionHtml.add("list", result);
            //html.inspect();

//            BeanMap beanMap = new BeanMap(((List) skysailResponseAsObject).get(0));// new ListWrapper((List<?>)
//                                                                                   // skysailResponseAsObject));
//
//            HtmlRenderer renderer = new HtmlRenderer(template);
//            renderer.setRendererInput(new MapTransformer(beanMap).clean(new DefaultCleaningStrategy())
//                    .asRendererInput());
            page = page.replace("${content}", accordionHtml.render());
        }

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

    @SuppressWarnings("unchecked")
    private int handleDataElementsForList2(StringBuilder sb, int i, Object object, STGroupString template) {
        String accordionGroup = accordionGroupTemplate;
        i++;
        BeanMap beanMap = new BeanMap(object);

        HtmlRenderer renderer = new HtmlRenderer(template);
        renderer.setRendererInput(new MapTransformer(beanMap).clean(new DefaultCleaningStrategy()).asRendererInput());

        // String tmp =
        // "<table class='table table-hover table-bordered'>\n<tr><th colspan=2 style='background-color:#F5F5F5;'></th></tr>\n"
        // + renderer.render("mapIteration") + "</table>\n";

        // accordionGroup = accordionGroup.replace("${inner}", renderer.render("table"));
        // accordionGroup = accordionGroup.replace("${hlink}", renderer.render("header"));
        // accordionGroup = accordionGroup.replace("${index}", String.valueOf(i));
        // sb.append(accordionGroup).append("\n");
        sb.append(renderer.render("accordion"));
        return i;
    }

}
