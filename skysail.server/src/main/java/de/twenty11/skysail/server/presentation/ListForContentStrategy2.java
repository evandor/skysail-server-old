package de.twenty11.skysail.server.presentation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.STGroupString;

import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.utils.IOUtils;

public class ListForContentStrategy2 extends AbstractHtmlCreatingStrategy {

	private static Logger logger = LoggerFactory.getLogger(ListForContentStrategy2.class);
	
	private STGroupString template;

	public ListForContentStrategy2(BundleContext bundleContext,
			String templateSourceFile) {
		URL resource = bundleContext.getBundle().getResource(templateSourceFile);
		InputStream is;
		try {
			is = new BufferedInputStream(resource.openStream());
			template = new STGroupString(templateSourceFile,
					IOUtils.convertStreamToString(is), '%', '%');
		} catch (IOException e) {
			logger.error("Problem reading bundle resource '{}'", templateSourceFile);
			throw new RuntimeException(e);
		}

	}

	@Override
	public String createHtml(String page, Object skysailResponseAsObject,
			SkysailResponse<List<?>> skysailResponse) {
		StringBuilder sb = new StringBuilder(
				"<div class=\"accordion\" id=\"accordion2\">\n");
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

}
