package de.twenty11.skysail.server.presentation.stringtemplate;

import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.stringtemplate.v4.AttributeRenderer;

public class HtmlEscapeStringRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String s, Locale locale) {
        return (String) (s == null ? o : StringEscapeUtils.escapeHtml((String) o));
    }

}
