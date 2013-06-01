package de.twenty11.skysail.server.presentation;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.BooleanNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.TextNode;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

public class Json2HtmlConverter extends JacksonConverter {

    private ObjectMapper m = new ObjectMapper();

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
            result = 0.9F;
        } else {
            result = 0.5F;
        }
        return result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        Variant jsonVariant = new Variant(MediaType.APPLICATION_JSON);
        Representation representation = super.toRepresentation(source, jsonVariant, resource);
        try {
            representation = new StringRepresentation(jsonToHtml(representation.getText(), resource));
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        representation.setMediaType(MediaType.TEXT_HTML);
        return representation;
    }

    public String jsonToHtml(String json, Resource resource) {

        JsonNode rootNode;
        StringBuffer html = new StringBuffer();
        try {
            rootNode = m.readValue(json, JsonNode.class);
            html = handleNode(null, rootNode, 1);
        } catch (Exception e) {
            // TODO
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer("<!DOCTYPE html>\n")
                .append("<html><head><title>Skysail Json Html Viewer</title>\n")
                .append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/../static/css/default.css\">\n")
                .append("<script type=\"text/javascript\" src=\"/../static/js/collapseAndExplode.js\"></script>\n")
                .append("</head>\n<body>skysail server<br><hr>\n")
                .append("<div id=\"json\">\n")
                .append(html)
                .append("\n</div>\n<hr><span class=\"poweredby\">skysail server version 0.0.0 (build date 0.0.0)</span></body>\n</html>");
        return sb.toString();
    }
    
    private StringBuffer handleNode(String key, JsonNode rootNode, int indentation) {
        StringBuffer html = new StringBuffer();

        if (rootNode instanceof ObjectNode) {
            handleObjectNode(rootNode, indentation, html);
        } else if (rootNode instanceof ArrayNode) {
            handleArrayNode(rootNode, indentation, html);
        } else if (rootNode instanceof TextNode) {
            if (rootNode.toString().startsWith("\"http://") || rootNode.toString().startsWith("\"https://")) {
                html.append("<span class=\"string\"><a href=" + rootNode + ">" + rootNode + "</a></span>");
            } else if ("message".equals(key) && indentation == 3) {
                html.append("<span class=\"message\">" + rootNode + "</span>");
            } else {
                html.append("<span class=\"string\">" + rootNode + "</span>");
            }
        } else if (rootNode instanceof IntNode) {
            html.append("<span class=\"num\">" + rootNode + "</span>");
        } else if (rootNode instanceof BooleanNode) {
            html.append("<span class=\"bool\">" + rootNode + "</span>");
        } else if (rootNode instanceof NullNode) {
            html.append("<span class=\"bool\">" + rootNode + "</span>");
        } else {
            html.append("<span class=\"unknown\">" + rootNode + "</span>");
            // throw new IllegalStateException("cannot handle node of type " + rootNode.getClass().getCanonicalName());
        }

        return html;
    }

    private void handleObjectNode(JsonNode rootNode, int indentation, StringBuffer html) {
        Iterator<Entry<String, JsonNode>> fields = rootNode.getFields();
        html.append("{\n").append(indent(indentation)).append("<ul class=\"obj collapsible\">\n");
        while (fields.hasNext()) {
            Entry<String, JsonNode> next = fields.next();
            String key = next.getKey();
            JsonNode value = next.getValue();
            html.append(indent(indentation + 1)).append("<li>\n");
            html.append(indent(indentation + 2)).append(
                    "<span class=\"prop\"><span class=\"q\">\"</span>" + key + "<span class=\"q\">\"</span></span> : ");
            html.append(handleNode(key, value, indentation + 2) + "\n");
            if (fields.hasNext()) {
                html.append(indent(indentation + 2)).append(",\n");
            }
            html.append(indent(indentation + 1)).append("</li>\n");
        }
        html.append(indent(indentation)).append("</ul>\n");
        html.append(indent(indentation)).append("}\n");
    }

    private void handleArrayNode(JsonNode rootNode, int indentation, StringBuffer html) {
        Iterator<JsonNode> iterator = rootNode.iterator();
        html.append("[\n").append(indent(indentation)).append("<ul class=\"array collapsible\">\n");
        while (iterator.hasNext()) {
            JsonNode value = iterator.next();
            html.append(indent(indentation + 2)).append("<li>\n");
            html.append(indent(indentation + 3)).append(handleNode(null, value, indentation + 2) + "\n");
            if (iterator.hasNext()) {
                html.append(indent(indentation + 3)).append(",\n");
            }
            html.append(indent(indentation + 2)).append("</li>\n");
        }
        html.append(indent(indentation)).append("</ul>\n");
        html.append(indent(indentation + 1)).append("]\n");
    }



    private String indent(int i) {
        return StringUtils.repeat("  ", i);
    }

}
