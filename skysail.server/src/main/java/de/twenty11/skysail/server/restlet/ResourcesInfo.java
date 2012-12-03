package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.Reference;

public class ResourcesInfo extends DocumentedInfo {
    /** Base URI for each child resource identifier. */
    private Reference baseRef;

    /** List of child resources. */
    private List<ResourceInfo> resources;

    /**
     * Constructor.
     */
    public ResourcesInfo() {
        super();
    }
    
    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResourcesInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public ResourcesInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResourcesInfo(String documentation) {
        super(documentation);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("\n");
        sb.append("Resources: ").append(resources).append("");
        return sb.toString();
    }

    /**
     * Returns the base URI for each child resource identifier.
     * 
     * @return The base URI for each child resource identifier.
     */
    public Reference getBaseRef() {
        return this.baseRef;
    }

    /**
     * Returns the list of child resources.
     * 
     * @return The list of child resources.
     */
    public List<ResourceInfo> getResources() {
        // Lazy initialization with double-check.
        List<ResourceInfo> r = this.resources;
        if (r == null) {
            synchronized (this) {
                r = this.resources;
                if (r == null) {
                    this.resources = r = new ArrayList<ResourceInfo>();
                }
            }
        }
        return r;
    }

    /**
     * Sets the base URI for each child resource identifier.
     * 
     * @param baseRef
     *            The base URI for each child resource identifier.
     */
    public void setBaseRef(Reference baseRef) {
        this.baseRef = baseRef;
    }

    /**
     * Sets the list of child resources.
     * 
     * @param resources
     *            The list of child resources.
     */
    public void setResources(List<ResourceInfo> resources) {
        this.resources = resources;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());

    }


}