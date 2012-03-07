package de.twenty11.skysail.server.osgi.bundles;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.forms.FieldType;
import de.twenty11.skysail.common.forms.FormBuilder;
import de.twenty11.skysail.common.messages.FormData;
import de.twenty11.skysail.server.FormDataServerResource;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;

public class BundleResource extends FormDataServerResource {

    /** slf4j based logger implementation */
    // private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundleResource() {
        super(null);
        setTemplate("skysail.server.osgi.bundles:bundle.ftl");
    }

    @Override
    public FormData fillForm(FormData formData) {
        String bundleId = (String) getRequest().getAttributes().get(Constants.BUNDLE_ID);
        Bundle bundle = Bundles.getInstance().getBundle(Long.parseLong(bundleId));
        formData.set("id", Long.toString(bundle.getBundleId()));
        formData.set("name", bundle.getSymbolicName());
        formData.set("state", new Integer(bundle.getState()).toString());
        formData.set("lastModified", new Long(bundle.getLastModified()).toString());
        formData.set("version", bundle.getVersion().toString());
        formData.set("location", bundle.getLocation());
        return formData;
    }

    @Override
    public void configureForm(FormBuilder builder) {
        // @formatter:off
        builder.addField("id",FieldType.TEXT)
        .addField("name", FieldType.TEXT)
        .addField("state", FieldType.TEXT)
        .addField("lastModified",  FieldType.TEXT)
        .addField("version",  FieldType.TEXT)
        .addField("location",  FieldType.TEXT)
        ;
        // @formatter:on
    }

}
