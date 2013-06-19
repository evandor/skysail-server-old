package de.twenty11.skysail.server.presentation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.beanutils.BeanMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.restlet.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private STGroupString headerTemplate;

    public ListForContentStrategy2(BundleContext bundleContext, Resource resource2, 
    		String templateSourceFile, String templateSourceFile4Header) {
        SkysailApplication currentApplication = (SkysailApplication) resource2.getApplication();
        Bundle currentBundle = currentApplication.getBundle();
        template = findTemplate(currentBundle, templateSourceFile);
        if (template == null) { // default template
            template = findTemplate(bundleContext.getBundle(), templateSourceFile);
        }
        headerTemplate = findTemplate(currentBundle, templateSourceFile4Header);
        if (headerTemplate == null) { // default template
            headerTemplate = findTemplate(bundleContext.getBundle(), templateSourceFile4Header);
        }
    }

    private STGroupString findTemplate(Bundle bundle, String templateSourceFile) {
        URL bundleResourceUrl = bundle.getResource(templateSourceFile);
        if (bundleResourceUrl == null) {
            return null;
        }
        InputStream is;
        try {
            is = new BufferedInputStream(bundleResourceUrl.openStream());
            return new STGroupString(templateSourceFile, IOUtils.convertStreamToString(is), '$', '$');
        } catch (IOException e) {
            logger.error("Problem reading bundle resource '{}'", templateSourceFile);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        StringBuilder sb = new StringBuilder("<div class=\"accordion\" id=\"accordion2\">\n");
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            int i = 0;
            if (data != null) {
                for (Object object : data) {
                    i = handleDataElementsForList2(sb, i, object, template);
                }
            }
        } else {
            handleDataElementsForList2(sb, 1, skysailResponseAsObject, template);
        }
        sb.append("</div>\n");
        page = page.replace("${content}", sb.toString());
        return page;
    }

    private int handleDataElementsForList2(StringBuilder sb, int i, Object object, STGroupString template) {
        String accordionGroup = accordionGroupTemplate;
        i++;
        BeanMap beanMap = new BeanMap(object);

        HtmlRenderer renderer = new HtmlRenderer(template);
        renderer.setRendererInput(new MapTransformer(beanMap).clean(new DefaultCleaningStrategy()).asRendererInput());

        HtmlRenderer headerRenderer = new HtmlRenderer(headerTemplate);
        headerRenderer.setRendererInput(new MapTransformer(beanMap).clean(new DefaultCleaningStrategy()).asRendererInput());
        headerRenderer.render("header");
        
        
        String tmp = "<table class='table table-hover table-bordered'>\n<tr><th colspan=2 style='background-color:#F5F5F5;'></th></tr>\n"
                + renderer.render("mapIteration") + "</table>\n";

        accordionGroup = accordionGroup.replace("${inner}", tmp);
        accordionGroup = accordionGroup.replace("${index}", String.valueOf(i));
        sb.append(accordionGroup).append("\n");
        return i;
    }

}
