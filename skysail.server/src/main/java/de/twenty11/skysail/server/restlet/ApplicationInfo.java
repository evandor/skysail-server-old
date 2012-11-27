package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationInfo extends DocumentedInfo {

    /** Container for definitions of the format of data exchanged. */
    //private GrammarsInfo grammars;

    /** List of methods. */
    private List<MethodInfo> methods;

    /**
     * Map of namespaces used in the WADL document. The key is the URI of the
     * namespace and the value, the prefix.
     */
    private Map<String, String> namespaces;

    /** List of representations. */
    //private List<RepresentationInfo> representations;

    /** Resources provided by the application. */
    //private ResourcesInfo resources;

    /**
     * Describes a set of methods that define the behavior of a type of
     * resource.
     */
    //private List<ResourceTypeInfo> resourceTypes;

    /**
     * Constructor.
     */
    public ApplicationInfo() {
        super();
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ApplicationInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public ApplicationInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ApplicationInfo(String documentation) {
        super(documentation);
    }

    /**
     * Returns the grammar elements.
     * 
     * @return The grammar elements.
     */
//    public GrammarsInfo getGrammars() {
//        return this.grammars;
//    }

    /**
     * Returns the list of method elements.
     * 
     * @return The list of method elements.
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
     * Returns the map of namespaces used in the WADL document.
     * 
     * @return The ap of namespaces used in the WADL document.
     */
    public Map<String, String> getNamespaces() {
        // Lazy initialization with double-check.
        Map<String, String> n = this.namespaces;
        if (n == null) {
            synchronized (this) {
                n = this.namespaces;
                if (n == null) {
                    this.namespaces = n = new HashMap<String, String>();
                }
            }
        }
        return n;
    }

    /**
     * Returns the list of representation elements.
     * 
     * @return The list of representation elements.
     */
//    public List<RepresentationInfo> getRepresentations() {
//        // Lazy initialization with double-check.
//        List<RepresentationInfo> r = this.representations;
//        if (r == null) {
//            synchronized (this) {
//                r = this.representations;
//                if (r == null) {
//                    this.representations = r = new ArrayList<RepresentationInfo>();
//                }
//            }
//        }
//        return r;
//    }

    /**
     * Returns the resources root element.
     * 
     * @return The resources root element.
     */
//    public ResourcesInfo getResources() {
//        // Lazy initialization with double-check.
//        ResourcesInfo r = this.resources;
//        if (r == null) {
//            synchronized (this) {
//                r = this.resources;
//                if (r == null) {
//                    this.resources = r = new ResourcesInfo();
//                }
//            }
//        }
//        return r;
//    }

    /**
     * Returns the list of resource type elements.
     * 
     * @return The list of resource type elements.
     */
//    public List<ResourceTypeInfo> getResourceTypes() {
//        // Lazy initialization with double-check.
//        List<ResourceTypeInfo> rt = this.resourceTypes;
//        if (rt == null) {
//            synchronized (this) {
//                rt = this.resourceTypes;
//                if (rt == null) {
//                    this.resourceTypes = rt = new ArrayList<ResourceTypeInfo>();
//                }
//            }
//        }
//        return rt;
//    }

    /**
     * Sets the grammars element.
     * 
     * @param grammars
     *            The grammars element.
     */
//    public void setGrammars(GrammarsInfo grammars) {
//        this.grammars = grammars;
//    }

    /**
     * Sets the list of documentation elements.
     * 
     * @param methods
     *            The list of method elements.
     */
    public void setMethods(List<MethodInfo> methods) {
        this.methods = methods;
    }

    /**
     * Sets the map of namespaces used in the WADL document. The key is the URI
     * of the namespace and the value, the prefix.
     * 
     * @param namespaces
     *            The map of namespaces used in the WADL document.
     */
    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());
//
//        if (getGrammars() != null) {
//            getGrammars().updateNamespaces(namespaces);
//        }
//
//        for (final MethodInfo methodInfo : getMethods()) {
//            methodInfo.updateNamespaces(namespaces);
//        }
//
//        for (final RepresentationInfo representationInfo : getRepresentations()) {
//            representationInfo.updateNamespaces(namespaces);
//        }
//
//        if (getResources() != null) {
//            getResources().updateNamespaces(namespaces);
//        }
//
//        for (final ResourceTypeInfo resourceTypeInfo : getResourceTypes()) {
//            resourceTypeInfo.updateNamespaces(namespaces);
//        }
    }



}