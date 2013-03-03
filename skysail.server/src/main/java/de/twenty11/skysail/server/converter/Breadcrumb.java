package de.twenty11.skysail.server.converter;

public final class Breadcrumb {

    private final String href;
    private final String cssClass;
    private final String value;

    public Breadcrumb(String href, String cssClass, String value) {
        this.href = href;
        this.cssClass = cssClass;
        this.value = value;
    }

    public String getHref() {
        return href;
    }

    public String getCssClass() {
        return cssClass;
    }

    public String getValue() {
        return value;
    }
}
