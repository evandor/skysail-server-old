package de.twenty11.skysail.server.restlet;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Reference;
import org.restlet.ext.wadl.ApplicationInfo;
import org.restlet.ext.wadl.ResourceInfo;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.responses.FailureResponse;
import de.twenty11.skysail.common.responses.Response;
import de.twenty11.skysail.common.responses.SuccessResponse;

/**
 * By implementing this class you can provide RESTful access to a specific resource, i.e. a RESTful representation of
 * the resource.
 * 
 * The type of the representation depends on the request. Currently JSON, XML and HTML are supported.
 * 
 * If an exception occurs, skysail will create an error-representation with information about the exception with the
 * same type (Json, xml,...).
 * 
 * @param <T>
 *            has to extend the marker interface SkysailData.
 */
public abstract class SkysailServerResource2<T> extends ServerResource {

    /** slf4j based logger implementation. */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** short message to be passed to the client. */
    private String message = "";

    /** template to be shown. */
    private String template = "skysail.product.twindir:accounts.ftl";

    /** the payload. */
    private T skysailData;

    /** Resource should be described itself via JSON (when handling OPTIONS request). */
    private volatile boolean autoDescribing = true;

    /** The description of "self". */
    private volatile String description;

    /**
     * The name of this documented resource. Is seen as the title of the "doc" tag of the "resource" element in a WADL
     * document.
     */
    private volatile String name;

    public SkysailServerResource2() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Constructor expecting an object of type T (which will become the payload of the resource representation.
     * 
     * @param data
     *            for example new GridData()
     */
    public SkysailServerResource2(T data) {
        this.skysailData = data;
    }

    /**
     * to be implemented by extending classes.
     * 
     * @return SkysailData
     */
    public abstract T getFilteredData();

    /**
     * Reasoning: not overwriting those two (overloaded) methods gives me a jackson deserialization issue. I need to
     * define which method I want to be ignored by jackson.
     * 
     * @see org.restlet.resource.ServerResource#setLocationRef(org.restlet.data.Reference)
     */
    @JsonIgnore
    @Override
    public void setLocationRef(Reference locationRef) {
        super.setLocationRef(locationRef);
    }

    @Override
    public void setLocationRef(String locationUri) {
        super.setLocationRef(locationUri);
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    protected String getParent() {
        if (getRequest() == null)
            return null;
        if (getRequest().getOriginalRef() == null)
            return null;
        if (getRequest().getOriginalRef().getParentRef() == null)
            return null;
        return getRequest().getOriginalRef().getParentRef().toString();
    }

    public T getSkysailData() {
        return skysailData;
    }

    public void setSkysailData(T skysailData) {
        this.skysailData = skysailData;
    }

    protected Response<T> createResponse() {
        Response<T> response;
        try {
            response = new SuccessResponse<T>(getFilteredData());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response = new FailureResponse<T>(e);
        }
        return response;
    }

    protected String determineValue(JSONObject jsonObject, String key) throws JSONException {
        if (jsonObject.isNull(key))
            return null;
        return jsonObject.getString(key);
    }

    // ===
    // Self describing stuff
    // ===

    @Override
    public Representation options() {
        if (autoDescribing) {
            return describe();
        }
        return null;
    }

    protected Representation describe() {
        Representation result = null;

        ResourceInfo resource = new ResourceInfo();
        describe(resource);
        ApplicationInfo application = resource.createApplication();
        describe(application);

        result = createJsonSelfRepresentation(application);

        return result;
    }
    
    public void describe(ResourceInfo info) {
        describe(getResourcePath(), info);
    }
    
    protected String getResourcePath() {
        Reference ref = new Reference(getRequest().getRootRef(), getRequest()
                .getResourceRef());
        return ref.getRemainingPart();
    }
    
    public void describe(String path, ResourceInfo info) {
        ResourceInfo.describe(null, info, this, path);
    }
    
    protected void describe(ApplicationInfo applicationInfo) {
    }

    protected void setName(String string) {
        // TODO Auto-generated method stub

    }

}
