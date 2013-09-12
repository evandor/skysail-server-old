package de.twenty11.skysail.server.presentation.stringtemplate;

import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.stringtemplate.v4.AttributeRenderer;

public class FormRenderer implements AttributeRenderer {

    @Override
    public String toString(Object o, String s, Locale locale) {
        String input = (String) o;
        if (s == null) {
            return input;
        }
        String escaped = StringEscapeUtils.escapeHtml(input);
        if (input.length() > 40) {
            return "<textarea name='" + s + "'>" + escaped + "</textarea";
        }
        return "<input type='text' name='" + s + "' value='" + escaped + "'>";
    }
}
