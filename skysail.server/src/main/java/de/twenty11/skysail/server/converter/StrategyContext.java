package de.twenty11.skysail.server.converter;

import java.util.List;

import de.twenty11.skysail.common.responses.SkysailResponse;

public class StrategyContext implements HtmlCreatingStrategy {

    private HtmlCreatingStrategy strategy;

    public StrategyContext(HtmlCreatingStrategy listForContentStrategy) {
        this.strategy = listForContentStrategy;
    }

    @Override
    public String createHtml(String page, Object skysailResponseAsObject, SkysailResponse<List<?>> skysailResponse) {
        return this.strategy.createHtml(page, skysailResponseAsObject, skysailResponse);
    }

}
