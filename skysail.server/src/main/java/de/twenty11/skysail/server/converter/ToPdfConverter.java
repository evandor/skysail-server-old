package de.twenty11.skysail.server.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.OutputRepresentation;
import org.restlet.representation.Representation;
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
public class ToPdfConverter extends ConverterHelper {

    public class PdfOutputRepresentation extends OutputRepresentation {


        private PDDocument document;

        public PdfOutputRepresentation(MediaType mediaType, PDDocument document) {
            super(mediaType);
            this.document = document;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException {
            try {
                document.save(outputStream);
            } catch (COSVisitorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static final VariantInfo VARIANT_JSON = new VariantInfo(MediaType.APPLICATION_EXCEL);

    @Override
    public float score(Object source, Variant target, Resource resource) {
        float result = -1.0F;
        if (!(source instanceof de.twenty11.skysail.common.responses.SkysailResponse)) {
            return 0.0F;
        }
        if (target.getMediaType().equals(MediaType.APPLICATION_PDF)) {
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
    public OutputRepresentation toRepresentation(Object source, Variant target, Resource resource) {
        OutputRepresentation representation;
        try {
            representation = toPdf((SkysailResponse) source, resource);
        } catch (Exception e) {
            representation = toPdf(new FailureResponse(e), resource);
        }
        representation.setMediaType(MediaType.TEXT_CSV);
        return representation;
    }

    private OutputRepresentation toPdf(SkysailResponse<List<?>> skysailResponse, Resource resource) {
        if (skysailResponse instanceof FailureResponse) {
            return null;// skysailResponse.getMessage();
        }

        Object skysailResponseAsObject = skysailResponse.getData();
        if (skysailResponseAsObject != null) {
            return createPdfForContent(skysailResponseAsObject);
        } else {
            return null;// "no data";
        }
    }

    private OutputRepresentation createPdfForContent(Object skysailResponseAsObject) {

        PDDocument document;
        try {
            document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDFont font = PDType1Font.HELVETICA_BOLD;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(font, 12);
            contentStream.moveTextPositionByAmount(100, 700);
            contentStream.drawString("Hello World");
            contentStream.endText();

            contentStream.close();
            PdfOutputRepresentation por = new PdfOutputRepresentation(MediaType.APPLICATION_PDF, document);

            document.close();
            return por;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        // StringBuilder sb = new StringBuilder("");
        // if (skysailResponseAsObject instanceof List) {
        // List<?> data = (List<?>) skysailResponseAsObject;
        // if (data != null) {
        // if (data.size() > 0) {
        // handleHeaderElementsForList(sb, data.get(0));
        // }
        // for (Object object : data) {
        // handleDataElementsForList(sb, object);
        // }
        // }
        // } else {
        // handleDataElementsForList(sb, skysailResponseAsObject);
        // }
        // sb.append("");
        // return sb.toString();
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
