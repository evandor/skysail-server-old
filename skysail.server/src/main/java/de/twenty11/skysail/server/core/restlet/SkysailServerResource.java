package de.twenty11.skysail.server.core.restlet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import de.twenty11.skysail.common.commands.Command;
import de.twenty11.skysail.common.navigation.LinkedPage;
import de.twenty11.skysail.common.responses.SkysailResponse;
import de.twenty11.skysail.server.restlet.SkysailApplication;

/**
 *
 */
public abstract class SkysailServerResource<T> extends ServerResource {

    public static final String CONTEXT_COMMANDS = "de.twenty11.skysail.server.restlet.SkysailServerResource2.commands";
    public static final String CONTEXT_LINKED_PAGES = "de.twenty11.skysail.server.restlet.SkysailServerResource2.linkedPages";

    /** short message to be passed to the client. */
    private String message = "";

    /** the payload. */
    private T skysailData;

    /** The description of "self". */
    private volatile String description;

    private volatile String name;

    public abstract T getData();

    public abstract T getData(Form form);

    public abstract String getMessage(String key);

    public abstract SkysailResponse<?> addEntity(T entity);

    @Override
    protected void doInit() throws ResourceException {
        if (getContext() != null) {
            getContext().getAttributes().put(CONTEXT_COMMANDS, new HashMap<String, Command>());
            getContext().getAttributes().put(CONTEXT_LINKED_PAGES, new ArrayList<LinkedPage>());
        }
    }

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

    protected String getResourcePath() {
        Reference ref = new Reference(getRequest().getRootRef(), getRequest().getResourceRef());
        return ref.getRemainingPart();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    protected void registerCommand(String key, Command command) {
        @SuppressWarnings("unchecked")
        Map<String, Command> commands = (Map<String, Command>) getContext().getAttributes().get(CONTEXT_COMMANDS);
        if (commands == null) {
            commands = new HashMap<String, Command>();
        }
        commands.put(key, command);
        ConcurrentMap<String, Object> attributes = getContext().getAttributes();
        attributes.put(CONTEXT_COMMANDS, commands);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Command> getCommands() {
        if (getContext() == null) {
            return Collections.emptyMap();
        }
        ConcurrentMap<String, Object> attributes = getContext().getAttributes();
        if (attributes.get(CONTEXT_COMMANDS) == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap((Map<String, Command>) attributes.get(CONTEXT_COMMANDS));
    }

    protected Command getCommand(String key) {
        @SuppressWarnings("unchecked")
        Map<String, Command> commands = (Map<String, Command>) getContext().getAttributes().get(CONTEXT_COMMANDS);
        if (commands == null) {
            return null;
        }
        return commands.get(key);
    }

    protected void registerLinkedPage(LinkedPage page) {
        // TODO check this: seems to be needed in tests only (ResourceTestWithUnguardedApplication)
        if (getContext() == null) {
            return;
        }
        @SuppressWarnings("unchecked")
        List<LinkedPage> pages = (List<LinkedPage>) getContext().getAttributes().get(CONTEXT_LINKED_PAGES);
        if (pages == null) {
            pages = new ArrayList<LinkedPage>();
        }
        pages.add(page);
        ConcurrentMap<String, Object> attributes = getContext().getAttributes();
        attributes.put(CONTEXT_LINKED_PAGES, pages);

    }

    protected LinkedPage addResourceLink(final String linkText, final Class<? extends ServerResource> sr) {
        LinkedPage linkedPage = new LinkedPage() {

            @Override
            public boolean applicable() {
                return true;
            }

            @Override
            public String getHref() {
                return ((SkysailApplication) getApplication()).getLinkTo(getRootRef(), sr);
            }

            @Override
            public String getLinkText() {
                return linkText;
            }
        };
        registerLinkedPage(linkedPage);
        return linkedPage;

    }

    @SuppressWarnings("unchecked")
    public List<LinkedPage> getLinkedPages() {
        if (getContext() == null) {
            return Collections.emptyList();
        }
        ConcurrentMap<String, Object> attributes = getContext().getAttributes();
        if (attributes.get(CONTEXT_LINKED_PAGES) == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList((List<LinkedPage>) attributes.get(CONTEXT_LINKED_PAGES));
    }

}
