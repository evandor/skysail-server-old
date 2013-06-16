package de.twenty11.skysail.server.presentation.render;

public class HtmlRenderer implements Renderer {

    private RendererInput rendererInput;

    public HtmlRenderer(RendererInput rendererInput) {
        this.rendererInput = rendererInput;
    }

    @Override
    public String render() {
        return "";
    }

}
