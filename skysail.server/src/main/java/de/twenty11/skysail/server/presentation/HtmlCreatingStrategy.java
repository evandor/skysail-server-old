package de.twenty11.skysail.server.presentation;

import java.util.List;

import de.twenty11.skysail.common.responses.SkysailResponse;

public interface HtmlCreatingStrategy {

    String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse);

}
