package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.Status;

public class ResponseInfo extends DocumentedInfo {

    /** List of parameters. */
    private List<ParameterInfo> parameters;

    /** List of representations. */
    private List<RepresentationInfo> representations;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("\n    [");
        sb.append("Parameters: ").append(parameters).append(",\n     ");
        sb.append("#Representations: ").append(representations.size()).append("]");
        return sb.toString();
    }

    /**
     * List of statuses associated with this response representation.
     */
    private List<Status> statuses;

    /**
     * Constructor.
     */
    public ResponseInfo() {
        super();
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResponseInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public ResponseInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public ResponseInfo(String documentation) {
        super(documentation);
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
     * Returns the list of representations
     * 
     * @return The list of representations
     */
    public List<RepresentationInfo> getRepresentations() {
        // Lazy initialization with double-check.
        List<RepresentationInfo> r = this.representations;
        if (r == null) {
            synchronized (this) {
                r = this.representations;
                if (r == null) {
                    this.representations = r = new ArrayList<RepresentationInfo>();
                }
            }
        }
        return r;
    }

    /**
     * Returns the list of statuses associated with this response
     * representation.
     * 
     * @return The list of statuses associated with this response
     *         representation.
     */
    public List<Status> getStatuses() {
        // Lazy initialization with double-check.
        List<Status> s = this.statuses;
        if (s == null) {
            synchronized (this) {
                s = this.statuses;
                if (s == null) {
                    this.statuses = s = new ArrayList<Status>();
                }
            }
        }
        return s;
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
     * Sets the list of representations
     * 
     * @param representations
     *            The list of representations
     */
    public void setRepresentations(List<RepresentationInfo> representations) {
        this.representations = representations;
    }

    /**
     * Sets the list of statuses associated with this response representation.
     * 
     * @param statuses
     *            The list of statuses associated with this response
     *            representation.
     */
    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());

        for (final RepresentationInfo representationInfo : getRepresentations()) {
            representationInfo.updateNamespaces(namespaces);
        }

        for (final ParameterInfo parameterInfo : getParameters()) {
            parameterInfo.updateNamespaces(namespaces);
        }
    }

    

}