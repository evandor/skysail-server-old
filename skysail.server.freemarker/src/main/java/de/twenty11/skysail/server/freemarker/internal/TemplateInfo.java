package de.twenty11.skysail.server.freemarker.internal;

import java.net.URL;

import org.osgi.framework.Bundle;

/**
 * Basically a bundle/url pair describing where to find a specific freemarker
 * template.
 * 
 * @author carsten
 * 
 */
public class TemplateInfo {

    /** magic number for hash calculation. */
    private static final int HASH_MULTIPLIER = 37;

    /** magic number for hash calculation. */
    private static final int HASH_BASE       = 17;

    /** osgi osgiBundle containing the freemarker template. */
    private Bundle           osgiBundle;

    /** the url of the freemarker template. */
    private URL              templateUrl;

    /** 
     * the timestamp of the file containing the template.
     * During development, a different source for the template might be choosen so that
     * you dont always have to create the jar where the template usually is contained.
     */
    private long timestamp;

    /**
     * 
     */
    private String locationInBundle;
    
    /**
     * creates a new object describing where to find a ftl freemarker template
     * in the context of OSGi.
     * 
     * The url might look different for different OSGi frameworks
     * 
     * @param bundle
     *            the osgi bundle containing the template
     * @param url
     *            the exact url inside that bundle (e.g. bundleentry://5.fwk357451187/freemarker/ftls/description.ftl)
     * @param locationInBundle 
     */
    public TemplateInfo(final Bundle bundle, final URL url, String locationInBundle) {
        this.osgiBundle = bundle;
        this.templateUrl = url;
        this.locationInBundle = locationInBundle;
    }

    /**
     * @return the url of the template
     */
    public final URL getTemplateURL() {
        return templateUrl;
    }
    
    /**
     * @param timestamp
     */
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
    
    /**
     * @return
     */
    public final long getTimestamp() {
        return timestamp;
    }
    
    public String getLocationInBundle() {
        return locationInBundle;
    }

    /**
     * 
     * @param that
     *            the other object
     * @return true if equal
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object that) {
        if (this == that) {
            return true;
        }
        if (!(that instanceof TemplateInfo)) {
            return false;
        }
        TemplateInfo otherTemplete = (TemplateInfo) that;
        return templateUrl.toString().equals(otherTemplete.templateUrl.toString())
                && osgiBundle.getBundleId() == otherTemplete.osgiBundle.getBundleId();
    }

    /**
     * @see java.lang.Object#hashCode()
     * @return calculated hash
     */
    @Override
    public final int hashCode() {
        int result = TemplateInfo.HASH_BASE;
        result = TemplateInfo.HASH_MULTIPLIER * result + templateUrl.toString().hashCode();
        result = TemplateInfo.HASH_MULTIPLIER * result + osgiBundle.hashCode();
        return result;
    }

    @Override
    public final String toString() {
        return new StringBuffer("[").append(osgiBundle.getSymbolicName()).append(",").append(templateUrl).append("]")
                .toString();
    }

}
