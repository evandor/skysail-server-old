package de.twenty11.skysail.server.presentation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.Presentable2;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.utils.IOUtils;

public abstract class AbstractHtmlCreatingStrategy implements HtmlCreatingStrategy {

	private static final Logger logger = LoggerFactory.getLogger(AbstractHtmlCreatingStrategy.class);
	
    protected String accordionGroupTemplate;

	public AbstractHtmlCreatingStrategy() {
        InputStream accordionGroupTemplateResource = this.getClass().getResourceAsStream("accordionGroup.template");
        accordionGroupTemplate = IOUtils.convertStreamToString(accordionGroupTemplateResource);
        try {
			accordionGroupTemplateResource.close();
		} catch (IOException e) {
			logger.error("Problem closing resource",e);
		}
	}
    
    @Override
    public abstract String createHtml(String page, Object skysailResponseAsObject,
            SkysailResponse<List<?>> skysailResponse);

    protected int handleDataElementsForList(StringBuilder sb, int i, Object object) {
        String accordionGroup = accordionGroupTemplate;
        i++;
        if (object instanceof Presentable2) {
            Presentable2 presentable = (Presentable2) object;
            accordionGroup = presentable.getHtml();
        } else {
            accordionGroup = accordionGroup.replace("${headerText}", "Entry #" + i);
            accordionGroup = accordionGroup.replace("${headerImage}", "icon-list-alt");
            accordionGroup = accordionGroup.replace("${headerCategoryIcon}", "");
            accordionGroup = accordionGroup.replace("${headerLink}", "");
            accordionGroup = accordionGroup.replace("${inner}", object.toString());
        }
        accordionGroup = accordionGroup.replace("${index}", String.valueOf(i));
        sb.append(accordionGroup).append("\n");
        return i;
    }



    

  
}
