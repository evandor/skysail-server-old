package de.twenty11.skysail.server.converter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.responses.Response;

public class Json2BootstrapConverter extends ConverterHelper {

    private ObjectMapper m = new ObjectMapper();

    private InputStream bootstrapTemplateResource = this.getClass().getResourceAsStream("bootstrap.template");
    private final String rootTemplate = convertStreamToString(bootstrapTemplateResource);

    private InputStream accordionGroupTemplateResource = this.getClass().getResourceAsStream("accordionGroup.template");
    private final String accordionGroupTemplate = convertStreamToString(accordionGroupTemplateResource);

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_JSON);

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.Response)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.TEXT_HTML)) {
            result = 1.0F;
        } else {
            result = 0.5F;
        }
        return result;
    }

    @Override
    public <T> float score(Representation source, Class<T> target, Resource resource) {
        float result = -1.0F;

        if (source instanceof JacksonRepresentation<?>) {
            result = 1.0F;
        } else if ((target != null) && JacksonRepresentation.class.isAssignableFrom(target)) {
            result = 1.0F;
        } else if (VARIANT_JSON.isCompatible(source)) {
            result = 0.8F;
        }

        return result;
    }

    @Override
    public <T> T toObject(Representation source, Class<T> target, Resource resource) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Class<?>> getObjectClasses(Variant source) {
        List<Class<?>> result = null;

        if (VARIANT_JSON.isCompatible(source)) {
            result = addObjectClass(result, Object.class);
            result = addObjectClass(result, JacksonRepresentation.class);
        }

        return result;
    }

    @Override
    public List<VariantInfo> getVariants(Class<?> source) {
        List<VariantInfo> result = null;

        if (source != null) {
            result = addVariant(result, VARIANT_JSON);
        }

        return result;
    }

    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        Variant jsonVariant = new Variant(MediaType.APPLICATION_JSON);
        // Representation representation = super.toRepresentation(source, jsonVariant, resource);
        Representation representation = new StringRepresentation(jsonToHtml((Response) source, resource));
        representation.setMediaType(MediaType.TEXT_HTML);
        return representation;
    }

    private String jsonToHtml(Response<List<?>> skysailResponse, Resource resource) {
        String page = rootTemplate;
        // template = template.replace("${originalJson}", json);
        long executionTimeInNanos = skysailResponse.getExecutionTime();
        float performance = new Long(1000000000) / executionTimeInNanos;
        page = page.replace("${performance}", String.format("%s", performance));
        page = page.replace("${result}", skysailResponse.getSuccess() ? "success" : "failure!");
        page = page.replace("${message}", skysailResponse.getMessage() == null ? "no message available"
                : skysailResponse.getMessage());
        page = page.replace("${presentations}", presentations());
        List<?> data = skysailResponse.getData();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object object : data) {
            String accordionGroup = accordionGroupTemplate;
            i++;
            if (object instanceof Presentable) {
                Presentable presentable = (Presentable) object;
                accordionGroup = accordionGroup.replace("${header}", presentable.getHeader());
                accordionGroup = accordionGroup.replace("${image}", presentable.getImageIdentifier());
                accordionGroup = accordionGroup.replace("${inner}", getInner(presentable));
                accordionGroup = accordionGroup.replace("${headerlink}", headerlink(presentable));
            } else {
                accordionGroup = accordionGroup.replace("${header}", "Entry " + i);
                accordionGroup = accordionGroup.replace("${image}", "icon-list-alt");
                accordionGroup = accordionGroup.replace("${inner}", object.toString());
                accordionGroup = accordionGroup.replace("${headerlink}", "");
            }
            accordionGroup = accordionGroup.replace("${index}", String.valueOf(i));
            sb.append(accordionGroup).append("\n");
        }
        page = page.replace("${accordionGroups}", sb.toString());
        return page;
    }

    private String headerlink(Presentable presentable) {
        if (presentable.getHeaderLink() == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='").append(presentable.getHeaderLink()).append("'><i class='icon-chevron-right'></i></a>\n");
        return sb.toString();
    }

    private String presentations() {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='?media=json'>Json</a>\n");
        return sb.toString();
    }

    private String getInner(Presentable presentable) {
        StringBuilder sb = new StringBuilder("<table class=\"table table-hover\" style='width:90%'>\n");
        for (Entry<String, Object> row : presentable.getContent().entrySet()) {
            sb.append("<tr>\n");
            sb.append("<td style='width:200px;'>").append(row.getKey()).append("</td>");
            sb.append("<td style='width:600px;'>").append(row.getValue()).append("</td>\n");
            sb.append("</td>\n");
        }
        return sb.append("</table>\n").toString();
    }

    public static String convertStreamToString(java.io.InputStream is) {
        @SuppressWarnings("resource")
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
