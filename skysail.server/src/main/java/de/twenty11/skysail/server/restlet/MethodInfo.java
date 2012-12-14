package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.engine.resource.AnnotationInfo;
import org.restlet.engine.resource.AnnotationUtils;
import org.restlet.representation.Variant;
import org.restlet.resource.ServerResource;
import org.restlet.service.MetadataService;

public class MethodInfo extends DocumentedInfo {

    /** Identifier for the method. */
    private String identifier;

    /** Name of the method. */
    private Method name;

    /** Describes the input to the method. */
    private RequestInfo request;

    /** Describes the output of the method. */
    private List<ResponseInfo> responses;

    /** Reference to a method definition element. */
    private Reference targetRef;

    /**
     * Constructor.
     */
    public MethodInfo() {
        super();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("\n  ['");
        sb.append(name).append("',\n   ");
        sb.append("RequestInfo: ").append(request).append(",\n   ");
        sb.append("Responses: ").append(responses).append(",\n   ");
        sb.append("TargetRef: ").append(targetRef).append("\n  ]");
        return sb.toString();
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public MethodInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public MethodInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public MethodInfo(String documentation) {
        super(documentation);
    }
    
    /**
     * Automatically describe a method by discovering the resource's
     * annotations.
     * 
     * @param info
     *            The method description to update.
     * @param resource
     *            The server resource to describe.
     */
    public static void describeAnnotations(MethodInfo info,
            ServerResource resource) {
        // Loop over the annotated Java methods
        MetadataService metadataService = resource.getMetadataService();
        List<AnnotationInfo> annotations = resource.isAnnotated() ? AnnotationUtils
                .getAnnotations(resource.getClass()) : null;

        if (annotations != null && metadataService != null) {
            for (AnnotationInfo annotationInfo : annotations) {
                if (info.getName().equals(annotationInfo.getRestletMethod())) {
                    // Describe the request
                    Class<?>[] classes = annotationInfo.getJavaInputTypes();

                    List<Variant> requestVariants = annotationInfo
                            .getRequestVariants(resource.getMetadataService(),
                                    resource.getConverterService());

                    if (requestVariants != null) {
                        for (Variant variant : requestVariants) {
                            if ((variant.getMediaType() != null)
                                    && ((info.getRequest() == null) || !info
                                            .getRequest().getRepresentations()
                                            .contains(variant))) {
                                RepresentationInfo representationInfo = null;

                                if (info.getRequest() == null) {
                                    info.setRequest(new RequestInfo());
                                }

                                if (resource instanceof SkysailServerResource2) {
                                    representationInfo = ((SkysailServerResource2) resource)
                                            .describe(info, info.getRequest(),
                                                    classes[0], variant);
                                } else {
                                    representationInfo = new RepresentationInfo(
                                            variant);
                                }

                                info.getRequest().getRepresentations()
                                        .add(representationInfo);
                            }
                        }
                    }

                    // Describe the response
                    Class<?> outputClass = annotationInfo.getJavaOutputType();

                    if (outputClass != null) {
                        List<Variant> responseVariants = annotationInfo
                                .getResponseVariants(
                                        resource.getMetadataService(),
                                        resource.getConverterService());

                        if (responseVariants != null) {
                            for (Variant variant : responseVariants) {
                                if ((variant.getMediaType() != null)
                                        && !info.getResponse()
                                                .getRepresentations()
                                                .contains(variant)) {
                                    RepresentationInfo representationInfo = null;

                                    if (resource instanceof SkysailServerResource2) {
                                        representationInfo = ((SkysailServerResource2) resource)
                                                .describe(info,
                                                        info.getResponse(),
                                                        outputClass, variant);
                                    } else {
                                        representationInfo = new RepresentationInfo(
                                                variant);
                                    }

                                    info.getResponse().getRepresentations()
                                            .add(representationInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the identifier for the method.
     * 
     * @return The identifier for the method.
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the name of the method.
     * 
     * @return The name of the method.
     */

    public Method getName() {
        return this.name;
    }

    /**
     * Returns the input to the method.
     * 
     * @return The input to the method.
     */
    public RequestInfo getRequest() {
        return this.request;
    }

    /**
     * Returns the last added response of the method.
     * 
     * @return The last added response of the method.
     */
    public ResponseInfo getResponse() {
        if (getResponses().isEmpty()) {
            getResponses().add(new ResponseInfo());
        }

        return getResponses().get(getResponses().size() - 1);
    }

    /**
     * Returns the output of the method.
     * 
     * @return The output of the method.
     */
    public List<ResponseInfo> getResponses() {
        // Lazy initialization with double-check.
        List<ResponseInfo> r = this.responses;
        if (r == null) {
            synchronized (this) {
                r = this.responses;
                if (r == null) {
                    this.responses = r = new ArrayList<ResponseInfo>();
                }
            }
        }
        return r;
    }

    /**
     * Returns the reference to a method definition element.
     * 
     * @return The reference to a method definition element.
     */
    public Reference getTargetRef() {
        return this.targetRef;
    }

    /**
     * Sets the identifier for the method.
     * 
     * @param identifier
     *            The identifier for the method.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the name of the method.
     * 
     * @param name
     *            The name of the method.
     */
    public void setName(Method name) {
        this.name = name;
    }

    /**
     * Sets the input to the method.
     * 
     * @param request
     *            The input to the method.
     */
    public void setRequest(RequestInfo request) {
        this.request = request;
    }

    /**
     * Sets the output of the method.
     * 
     * @param responses
     *            The output of the method.
     */
    public void setResponses(List<ResponseInfo> responses) {
        this.responses = responses;
    }

    /**
     * Sets the reference to a method definition element.
     * 
     * @param targetRef
     *            The reference to a method definition element.
     */
    public void setTargetRef(Reference targetRef) {
        this.targetRef = targetRef;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());

    }



}