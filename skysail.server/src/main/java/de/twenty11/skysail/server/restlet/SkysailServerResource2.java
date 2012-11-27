package de.twenty11.skysail.server.restlet;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.ServerResource;

/**
 *
 */
public abstract class SkysailServerResource2<T> extends ServerResource {

    /** short message to be passed to the client. */
    private String message = "";

    /** the payload. */
    private T skysailData;

    /** Resource should be described itself via JSON (when handling OPTIONS request). */
    private volatile boolean autoDescribing = true;

    /** The description of "self". */
    private volatile String description;

    private volatile String name;

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

    private Representation createJsonSelfRepresentation(ApplicationInfo applicationInfo) {
        return new JsonSelfRepresentation(applicationInfo);
    }

    public void describe(ResourceInfo info) {
        describe(getResourcePath(), info);
    }

    protected String getResourcePath() {
        Reference ref = new Reference(getRequest().getRootRef(), getRequest().getResourceRef());
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

    public List<ParameterInfo> describeParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    protected void describeMethod(Method method, MethodInfo info) {
        info.setName(method);

        if (Method.GET.equals(method)) {
            describeGet(info);
        } else if (Method.POST.equals(method)) {
            describePost(info);
        } else if (Method.PUT.equals(method)) {
            describePut(info);
        } else if (Method.DELETE.equals(method)) {
            describeDelete(info);
        } else if (Method.OPTIONS.equals(method)) {
            describeOptions(info);
        }
    }

    /**
     * Describes the GET method.<br>
     * By default, it describes the response with the available variants based on the {@link #getVariants()} method.
     * Thus in the majority of cases, the method of the super class must be called when overridden.
     * 
     * @param info
     *            The method description to update.
     */
    protected void describeGet(MethodInfo info) {
        MethodInfo.describeAnnotations(info, this);
    }

    /**
     * Describes the POST method.
     * 
     * @param info
     *            The method description to update.
     */
    protected void describePost(MethodInfo info) {
        MethodInfo.describeAnnotations(info, this);
    }

    /**
     * Describes the PUT method.
     * 
     * @param info
     *            The method description to update.
     */
    protected void describePut(MethodInfo info) {
        MethodInfo.describeAnnotations(info, this);
    }

    /**
     * Describes the DELETE method.
     * 
     * @param info
     *            The method description to update.
     */
    protected void describeDelete(MethodInfo info) {
        MethodInfo.describeAnnotations(info, this);
    }

    /**
     * Describes the OPTIONS method.<br>
     * By default it describes the response with the available variants based on the {@link #getWadlVariants()} method.
     * 
     * @param info
     *            The method description to update.
     */
    protected void describeOptions(MethodInfo info) {
        // Describe each variant
        // for (Variant variant : getWadlVariants()) {
        // RepresentationInfo result = new RepresentationInfo(variant);
        // info.getResponse().getRepresentations().add(result);
        // }
    }

    public RepresentationInfo describe(MethodInfo methodInfo, RequestInfo request, Class<?> representationClass, Variant variant) {
        return describe(methodInfo, representationClass, variant);
    }

    private RepresentationInfo describe(MethodInfo methodInfo, Class<?> representationClass, Variant variant) {
        return new RepresentationInfo(variant);
    }

    public RepresentationInfo describe(MethodInfo methodInfo, ResponseInfo response, Class<?> outputClass, Variant variant) {
        return describe(methodInfo, outputClass, variant);
    }

    

    

}
