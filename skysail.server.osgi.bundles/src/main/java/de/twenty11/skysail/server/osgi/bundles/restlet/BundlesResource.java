package de.twenty11.skysail.server.osgi.bundles.restlet;

import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;

import de.twenty11.skysail.common.grids.ColumnsBuilder;
import de.twenty11.skysail.common.grids.RowData;
import de.twenty11.skysail.server.osgi.bundles.internal.BundleUtils;
import de.twenty11.skysail.server.restlet.GridDataServerResource;

/**
 * A grid-based resource for bundles.
 * 
 * @author carsten
 * 
 */
public class BundlesResource extends GridDataServerResource {

	public BundlesResource() {
		super(new ColumnsBuilder() {
			@Override
			public void configure() {
				addColumn("id").setWidth(50).sortDesc(1);
				addColumn("symbolicName").setWidth(350).setWidth(100);
				addColumn("version").setWidth(100);
				addColumn("state").setWidth(50);
			}
		});
		setTemplate("skysail.server.osgi.bundles:bundles.ftl");
	}

	@Override
	public void buildGrid() {
		List<Bundle> bundles = BundleUtils.getInstance().getBundles();
		for (Bundle bundle : bundles) {
			RowData rowData = new RowData(getSkysailData().getColumns());
			rowData.add(bundle.getBundleId());
			rowData.add(bundle.getSymbolicName());
			rowData.add(bundle.getVersion());
			rowData.add(BundleUtils.translateStatus(bundle.getState()));
			getSkysailData().addRowData(rowData);
		}
	}
}
