package de.twenty11.skysail.server.restlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Variant;

public class RepresentationInfo extends DocumentedInfo {

    /** Identifier for that element. */
    private String identifier;

    /** Media type of that element. */
    private MediaType mediaType;

    /** List of parameters. */
    private List<ParameterInfo> parameters;

    /** List of locations of one or more meta data profiles. */
    private List<Reference> profiles;

    /** Reference to an representation identifier. */
    private String reference;

    /** Qualified name of the root element for this XML-based representation. */
    private String xmlElement;

    /**
     * Constructor.
     */
    public RepresentationInfo() {
        super();
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public RepresentationInfo(DocumentationInfo documentation) {
        super(documentation);
    }

    /**
     * Constructor with a list of documentation elements.
     * 
     * @param documentations
     *            The list of documentation elements.
     */
    public RepresentationInfo(List<DocumentationInfo> documentations) {
        super(documentations);
    }

    /**
     * Constructor with a media type.
     * 
     * @param mediaType
     *            The media type of the representation.
     */
    public RepresentationInfo(MediaType mediaType) {
        setMediaType(mediaType);
    }

    /**
     * Constructor with a single documentation element.
     * 
     * @param documentation
     *            A single documentation element.
     */
    public RepresentationInfo(String documentation) {
        super(documentation);
    }

    /**
     * Constructor with a variant.
     * 
     * @param variant
     *            The variant to describe.
     */
    public RepresentationInfo(Variant variant) {
        setMediaType(variant.getMediaType());
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
     * Returns the media type of that element.
     * 
     * @return The media type of that element.
     */
    public MediaType getMediaType() {
        return this.mediaType;
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
     * Returns the list of locations of one or more meta data profiles.
     * 
     * @return The list of locations of one or more meta data profiles.
     */
    public List<Reference> getProfiles() {
        // Lazy initialization with double-check.
        List<Reference> p = this.profiles;
        if (p == null) {
            synchronized (this) {
                p = this.profiles;
                if (p == null) {
                    this.profiles = p = new ArrayList<Reference>();
                }
            }
        }
        return p;
    }

    /**
     * Returns the reference to an representation identifier.
     * 
     * @return The reference to an representation identifier.
     */
    public String getReference() {
        return reference;
    }

    /**
     * Returns the qualified name of the root element for this XML-based
     * representation.
     * 
     * @return The qualified name of the root element for this XML-based
     *         representation.
     */
    public String getXmlElement() {
        return this.xmlElement;
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
     * Sets the media type of that element.
     * 
     * @param mediaType
     *            The media type of that element.
     */
    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
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
     * Sets the list of locations of one or more meta data profiles.
     * 
     * @param profiles
     *            The list of locations of one or more meta data profiles.
     */
//    public void setProfiles(List<Reference> profiles) {
//        this.profiles = profiles;
//    }

    /**
     * Sets the reference to an representation identifier.
     * 
     * @param reference
     *            The reference to an representation identifier.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Sets the qualified name of the root element for this XML-based
     * representation.
     * 
     * @param xmlElement
     *            The qualified name of the root element for this XML-based
     *            representation.
     */
    public void setXmlElement(String xmlElement) {
        this.xmlElement = xmlElement;
    }

    @Override
    public void updateNamespaces(Map<String, String> namespaces) {
        namespaces.putAll(resolveNamespaces());

//        for (final ParameterInfo parameterInfo : getParameters()) {
//            parameterInfo.updateNamespaces(namespaces);
//        }
    }


}