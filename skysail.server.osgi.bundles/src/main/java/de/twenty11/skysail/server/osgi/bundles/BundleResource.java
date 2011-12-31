package de.twenty11.skysail.server.osgi.bundles;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.twenty11.skysail.common.messages.FormData;
import de.twenty11.skysail.common.messages.TextFieldData;
import de.twenty11.skysail.server.osgi.bundles.internal.Bundles;
import de.twenty11.skysail.server.restletosgi.SkysailServerResource;

public class BundleResource extends SkysailServerResource<FormData> {

    /** slf4j based logger implementation */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BundleResource() {
        setTemplate("skysail.server.osgi.bundles:bundle.ftl");
    }

    @Override
    public FormData getData() {
        String bundleId = (String) getRequest().getAttributes().get(OsgiBundlesConstants.BUNDLE_ID);
        Bundle bundle = Bundles.getInstance().getBundle(Long.parseLong(bundleId));
        FormData form = new FormData();
        
        TextFieldData tfd = new TextFieldData("symbolicName", bundle.getSymbolicName());
        form.addField(tfd);
        
        tfd = new TextFieldData("id", Long.toString(bundle.getBundleId()));
        form.addField(tfd);

        tfd = new TextFieldData("state", Integer.toString(bundle.getState()));
        form.addField(tfd);

        tfd = new TextFieldData("lastModified", Long.toString(bundle.getLastModified()));
        form.addField(tfd);

        tfd = new TextFieldData("version", bundle.getVersion().toString());
        form.addField(tfd);

        tfd = new TextFieldData("location", bundle.getLocation());
        form.addField(tfd);

        return form;
    }

}
