package de.twenty11.skysail.server.converter;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.Resource;

import de.twenty11.skysail.common.Presentable;
import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.SkysailResponse;

/**
 * Proof of concept implementation
 * 
 * @author carsten
 * 
 */
public class ToCsvConverter extends ConverterHelper {

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_EXCEL);

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.TEXT_CSV)) {
            result = 1.0F;
        } else {
            result = 0.1F;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Representation toRepresentation(Object source, Variant target, Resource resource) {
        Representation representation;
        try {
            representation = new StringRepresentation(toCsv((SkysailResponse) source, resource));
        } catch (Exception e) {
            representation = new StringRepresentation(toCsv(new FailureResponse(e), resource));
        }
        representation.setMediaType(MediaType.TEXT_CSV);
        return representation;
    }

    private String toCsv(SkysailResponse<List<?>> skysailResponse, Resource resource) {
        if (skysailResponse instanceof FailureResponse) {
            return skysailResponse.getMessage();
        }

        Object skysailResponseAsObject = skysailResponse.getData();
        if (skysailResponseAsObject != null) {
            return createCsvForContent(skysailResponseAsObject);
        } else {
            return "no data";
        }
    }

    private String createCsvForContent(Object skysailResponseAsObject) {
        StringBuilder sb = new StringBuilder("");
        if (skysailResponseAsObject instanceof List) {
            List<?> data = (List<?>) skysailResponseAsObject;
            if (data != null) {
                if (data.size() > 0) {
                    handleHeaderElementsForList(sb, data.get(0));
                }
                for (Object object : data) {
                    handleDataElementsForList(sb, object);
                }
            }
        } else {
            handleDataElementsForList(sb, skysailResponseAsObject);
        }
        sb.append("");
        return sb.toString();
    }

    private void handleDataElementsForList(StringBuilder sb, Object object) {
        if (object instanceof Presentable) {
            Presentable presentable = (Presentable) object;
            boolean empty = true;
            for (Entry<String, Object> row : presentable.getContent().entrySet()) {
                sb.append(row.getValue() != null ? row.getValue().toString() : "".replace("\"", "\\\"")).append(",");
                empty = false;
            }
            if (!empty) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }
    }

    private void handleHeaderElementsForList(StringBuilder sb, Object object) {
        if (object instanceof Presentable) {
            Presentable presentable = (Presentable) object;
            boolean empty = true;
            for (Entry<String, Object> row : presentable.getContent().entrySet()) {
                sb.append(row.getKey().toString().replace("\"", "\\\"")).append(",");
                empty = false;
            }
            if (!empty) {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append("\n");
        }
    }

}
