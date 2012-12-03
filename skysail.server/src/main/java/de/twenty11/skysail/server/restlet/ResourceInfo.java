package de.twenty11.skysail.server.restlet;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.resource.ServerResource;

public class ResourceInfo extends DocumentedInfo {

    /** List of child resources. */
    private List<ResourceInfo> childResources;

    /** Identifier for that element. */
    private String identifier;

    /** List of supported methods. */
    private List<MethodInfo> methods;

    /** List of parameters. */
    private List<ParameterInfo> parameters;

    /** URI template for the identifier of the resource. */
    private String path;

    /** Media type for the query component of the resource URI. */
    private MediaType queryType;

    /** List of references to resource type elements. */
    private List<Reference> type;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("\n ");
        sb.append("Path: \"").append(path).append("\",\n ");
        sb.append("Identifier: ").append(identifier).append(",\n ");
        sb.append("Type: ").append(type).append(",\n ");
        sb.append("Methods supported: ").append(methods).append("");
        return sb.toString();
    }
    
    /**
     * Constructor.
     */
    public ResourceInfo() {
        super();
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResourceInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public ResourceInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResourceInfo(String documentation) {
        super(documentation);
    }

    /**
     * Returns a JSON description of the current resource.
     * 
     */
    public static void describe(ApplicationInfo applicationInfo,
            ResourceInfo info, Object resource, String path) {
        if ((path != null) && path.startsWith("/")) {
            path = path.substring(1);
        }

        info.setPath(path);

        // Introspect the current resource to detect the allowed methods
        List<Method> methodsList = new ArrayList<Method>();

        if (resource instanceof ServerResource) {
            ((ServerResource) resource).updateAllowedMethods();
            methodsList.addAll(((ServerResource) resource).getAllowedMethods());

            if (resource instanceof SkysailServerResource2) {
                info.setParameters(((SkysailServerResource2) resource)
                        .describeParameters());

                if (applicationInfo != null) {
                    ((SkysailServerResource2) resource).describe(applicationInfo);
                }
            }
        }

        Method.sort(methodsList);

        // Update the resource info with the description of the allowed methods
        List<MethodInfo> methods = info.getMethods();
        MethodInfo methodInfo;

        for (Method method : methodsList) {
            methodInfo = new MethodInfo();
            methods.add(methodInfo);
            methodInfo.setName(method);

            if (resource instanceof ServerResource) {
                if (resource instanceof SkysailServerResource2) {
                    SkysailServerResource2 wsResource = (SkysailServerResource2) resource;

                    //if (wsResource.canDescribe(method)) {
                        wsResource.describeMethod(method, methodInfo);
                    //}
                } else {
                    MethodInfo.describeAnnotations(methodInfo,
                            (ServerResource) resource);
                }
            }
        }

        // Document the resource
        String title = null;
        String textContent = null;

        if (resource instanceof SkysailServerResource2) {
//            title = ((SkysailServerResource2) resource).getName();
//            textContent = ((SkysailServerResource2) resource).getDescription();
        }

        if ((title != null) && !"".equals(title)) {
            DocumentationInfo doc = null;

            if (info.getDocumentations().isEmpty()) {
                doc = new DocumentationInfo();
                info.getDocumentations().add(doc);
            } else {
                info.getDocumentations().get(0);
            }

            doc.setTitle(title);
            //doc.setTextContent(textContent);
        }
    }


    /**
     * Creates an application descriptor that wraps this resource descriptor.
     * The title of the resource, that is to say the title of its first
     * documentation tag is transfered to the title of the first documentation
     * tag of the main application tag.
     * 
     * @return The new application descriptor.
     */
    public ApplicationInfo createApplication() {
        ApplicationInfo result = new ApplicationInfo();

        if (!getDocumentations().isEmpty()) {
            String titleResource = getDocumentations().get(0).getTitle();
            if (titleResource != null && !"".equals(titleResource)) {
                DocumentationInfo doc = null;

                if (result.getDocumentations().isEmpty()) {
                    doc = new DocumentationInfo();
                    result.getDocumentations().add(doc);
                } else {
                    doc = result.getDocumentations().get(0);
                }

                doc.setTitle(titleResource);
            }
        }

        ResourcesInfo resources = new ResourcesInfo();
        result.setResources(resources);
        resources.getResources().add(this);
        return result;
    }

    /**
     * Returns the list of child resources.
     * 
     * @return The list of child resources.
     */
    public List<ResourceInfo> getChildResources() {
        // Lazy initialization with double-check.
        List<ResourceInfo> r = this.childResources;
        if (r == null) {
            synchronized (this) {
                r = this.childResources;
                if (r == null) {
                    this.childResources = r = new ArrayList<ResourceInfo>();
                }
            }
        }
        return r;
    }

    /**
     * Returns the identifier for that element.
     * 
     * @return The identifier for that element.
     */
    public String getIdentifier() {
        return this.identifier;
    }

    /**
     * Returns the list of supported methods.
     * 
     * @return The list of supported methods.
     */
    public List<MethodInfo> getMethods() {
        // Lazy initialization with double-check.
        List<MethodInfo> m = this.methods;
        if (m == null) {
            synchronized (this) {
                m = this.methods;

                if (m == null) {
                    this.methods = m = new ArrayList<MethodInfo>();
                }
            }
        }
        return m;
    }

    /**
     * Returns the list of parameters.
     * 
     * @return The list of parameters.
     */
    public List<ParameterInfo> getParameters() {
        // Lazy initialization with double-check.
        List<ParameterInfo> p = this.parameters;
        if (p == null) {
            synchronized (this) {
                p = this.parameters;
                if (p == null) {
                    this.parameters = p = new ArrayList<ParameterInfo>();
                }
            }
        }
        return p;
    }

    /**
     * Returns the URI template for the identifier of the resource.
     * 
     * @return The URI template for the identifier of the resource.
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Returns the media type for the query component of the resource URI.
     * 
     * @return The media type for the query component of the resource URI.
     */
    public MediaType getQueryType() {
        return this.queryType;
    }

    /**
     * Returns the list of references to resource type elements.
     * 
     * @return The list of references to resource type elements.
     */
    public List<Reference> getType() {
        // Lazy initialization with double-check.
        List<Reference> t = this.type;
        if (t == null) {
            synchronized (this) {
                t = this.type;
                if (t == null) {
                    this.type = t = new ArrayList<Reference>();
                }
            }
        }
        return t;
    }

    /**
     * Sets the list of child resources.
     * 
     * @param resources
     *            The list of child resources.
     */
    public void setChildResources(List<ResourceInfo> resources) {
        this.childResources = resources;
    }

    /**
     * Sets the identifier for that element.
     * 
     * @param identifier
     *            The identifier for that element.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Sets the list of supported methods.
     * 
     * @param methods
     *            The list of supported methods.
     */
    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    /**
     * Sets the list of parameters.
     * 
     * @param parameters
     *            The list of parameters.
     */
    public void setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
    }

    /**
     * Sets the URI template for the identifier of the resource.
     * 
     * @param path
     *            The URI template for the identifier of the resource.
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Sets the media type for the query component of the resource URI.
     * 
     * @param queryType
     *            The media type for the query component of the resource URI.
     */
    public void setQueryType(MediaType queryType) {
        this.queryType = queryType;
    }

    /**
     * Sets the list of references to resource type elements.
     * 
     * @param type
     *            The list of references to resource type elements.
     */
    public void setType(List<Reference> type) {
        this.type = type;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());

    }


}
