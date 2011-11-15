package de.skysail.server.osgi.hibernate.session.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.skysail.server.osgi.hibernate.session.DynamicSessionFactoryProvider;

/**
 * keeps track of bundles being added or removed to update the list of annotated
 * classes to be considered for the dynamicSessionFactory.
 * 
 * This class is the activator for this OSGi bundle
 * 
 * @author carsten
 */
public class BundleTracker implements BundleListener, BundleActivator {

	/** string being looked for in the manifest */
	private String HIBERNATE_CONTRIBUTION = "Hibernate-Contribution";

	/** provided via injection */
	private static DynamicSessionFactoryProvider dsfProvider;

	private static Logger logger = LoggerFactory.getLogger(BundleTracker.class);

	private BundleContext bundleContext;

	private static BundleTracker instance;
	
	public BundleTracker() {
       System.out.println("new bundleTracker instantiated");
    }

	public void start(BundleContext context) {
		instance = this;
		bundleContext = context;
		startTrackingBundles();
	}

	public void stop(BundleContext context) {
		instance = null;
		bundleContext = null;
	}

	public void bundleChanged(BundleEvent bundleEvent) {
		try {
			logger.info("Bundle event received for bundle: "
					+ bundleEvent.getBundle().getSymbolicName());
				updateBundleClasses(bundleEvent.getBundle(), bundleEvent.getType());
		} catch (RuntimeException re) {
			logger.error("Error processing bundle event", re);
			throw re;
		}
	}

	public static void setDynamicConfiguration(
			DynamicSessionFactoryProvider dynamicConfiguration) {
		BundleTracker.dsfProvider = dynamicConfiguration;
		startTrackingBundles();
	}

	private void updateBundleClasses(Bundle bundle, int updateAction) {
		Object hibernateContribution = bundle.getHeaders().get(
				HIBERNATE_CONTRIBUTION);
		if (hibernateContribution != null) {
			String[] classes = parseHibernateClasses((String) hibernateContribution);
			switch (updateAction) {
			case BundleEvent.STARTED:
				logger.info("Adding classes from hibernate configuration: "
						+ writeArray(classes));
				dsfProvider.addAnnotatedClasses(bundle, bundleContext, classes);
				break;
			case BundleEvent.STOPPED:
				logger.info("Removing classes from hibernate configuration: "
						+ writeArray(classes));
				dsfProvider.removeAnnotatedClasses(bundle, classes);
				break;
			default:
				throw new IllegalArgumentException("" + updateAction);
			}
		}
	}

	private String writeArray(String[] classes) {
		if (classes.length == 0)
			return "";
		StringBuffer sb = new StringBuffer();
		for (String s : classes) {
			sb.append(s).append(", ");
		}
		return sb.toString().substring(0, sb.length() - 2);
	}

	private String[] parseHibernateClasses(String hibernateContribution) {
		String[] hcSplit = hibernateContribution.split(";");
		if (hcSplit.length != 2) {
			throwIllegalArgumentException();
		}
		// String dbConnection = hcSplit[0].trim();
		String classesString = hcSplit[1].trim();
		Pattern p = Pattern.compile("classes *= *\"(.*)\"");
		Matcher m = p.matcher(classesString);
		if (!m.find()) {
			throwIllegalArgumentException();
		}
		String[] classes = m.group(1).split(",");
		return classes;
	}

	private void throwIllegalArgumentException() {
		throw new IllegalArgumentException(
				"Hibernate-Contribution must be of the form "
						+ "Hibernate-Contribution: db-connection; class=\"org.example.model.SomeEntity\"");
	}

	/**
	 * Start tracking bundles after dynamic configuration has been initialized
	 */
	private static void startTrackingBundles() {
		logger.info("Have dynamic configuration: "
				+ (dsfProvider != null));
		logger.info("Have instance: " + (instance != null));
		if (dsfProvider != null && instance != null) {
			// now start tracking bundles
			logger.info("Starting to track bundle events");
			instance.bundleContext.addBundleListener(instance);
			instance.processBundles();
		}
	}

	/**
	 * process bundles started before I started listening for bundles
	 */
	private void processBundles() {
		for (Bundle b : instance.bundleContext.getBundles()) {
			if (b.getState() == Bundle.ACTIVE) {
				updateBundleClasses(b, BundleEvent.STARTED);
			}
		}
	}

}
