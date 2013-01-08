package de.twenty11.skysail.server.graphs.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import de.twenty11.skysail.common.graphs.Graph;
import de.twenty11.skysail.server.services.ApplicationDescriptor;
import de.twenty11.skysail.server.services.UrlMapper;

/**
 * A provider for graph models
 * 
 * 
 * 
 * @author carsten
 * 
 */
public class ContextGraphModelProvider implements GraphModelProvider {

    /**
     * the osgi bundle context provided in the constructor.
     */
    private final BundleContext bundleContext;

    public ContextGraphModelProvider(final BundleContext context) {
        this.bundleContext = context;
    }

    @Override
    public Map<ApplicationDescriptor, List<String>> getRelevantAppsAndPaths() {

        Map<ApplicationDescriptor, List<String>> result = new HashMap<ApplicationDescriptor, List<String>>();
        try {
            ServiceReference[] allSkysailApps = bundleContext.getAllServiceReferences(
                    ApplicationDescriptor.class.getName(), null);
            if (allSkysailApps != null) {
                for (ServiceReference serviceReference : allSkysailApps) {
                    ApplicationDescriptor skysailApp = (ApplicationDescriptor) bundleContext
                            .getService(serviceReference);
                    String skysailAppName = skysailApp.getApplicationDescription().getName();
                    List<String> paths = checkForGraphAnnotations(serviceReference.getBundle());
                    if (paths.size() > 0) {
                        result.put(skysailApp, paths);
                    }
                }
            }
            GraphsSkysailApplication.setGraphModelProvider(this);
            return result;
        } catch (InvalidSyntaxException e) {
            throw new RuntimeException("invalid syntax", e);
        }

    }

    private List<String> checkForGraphAnnotations(Bundle bundleToExamine) {
        List<String> paths = new ArrayList<String>();
        Enumeration<URL> classes = bundleToExamine.findEntries("/", "*.class", true);
        while (classes.hasMoreElements()) {
            try {
                URL nextElement = classes.nextElement();
                InputStream inputStream = nextElement.openConnection().getInputStream();
                DataInputStream dstream = new DataInputStream(new BufferedInputStream(inputStream));
                ClassFile cf = new ClassFile(dstream);
                Annotation graphAnnotation = getAnnotation(cf, Graph.class);
                if (graphAnnotation != null) {
                    String className = cf.getName();
                    List<String> pathsFromMapper = getPathsFromUrlMapper(className);
                    paths.addAll(pathsFromMapper);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return paths;
    }

    private List<String> getPathsFromUrlMapper(String className) throws Exception {
        List<String> results = new ArrayList<String>();
        ServiceReference[] allUrlMappers = bundleContext.getAllServiceReferences(UrlMapper.class.getName(), null);
        if (allUrlMappers != null) {
            for (ServiceReference serviceReference : allUrlMappers) {
                UrlMapper urlMapper = (UrlMapper) bundleContext.getService(serviceReference);
                if (urlMapper.getClass().equals(GraphsUrlMapper.class)) {
                    continue; // don't examine "self"
                }
                Map<String, String> urlMappings = urlMapper.provideUrlMapping();
                for (Entry<String, String> entry : urlMappings.entrySet()) {
                    if (entry.getValue().equals(className)) {
                        results.add(entry.getKey());
                    }
                }
            }
        }
        return results;
    }

    private Annotation getAnnotation(ClassFile cf, Class<?> annotation) {
        AnnotationsAttribute attribute = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);

        if (attribute == null) {
            return null;
        }
        Annotation nodeAnnotation = attribute.getAnnotation(annotation.getName());
        if (nodeAnnotation == null) {
            return null;
        }
        return nodeAnnotation;
    }

}
