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
import java.util.Set;

import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.common.forms.Form;
import de.twenty11.skysail.common.graphs.GraphDetails;
import de.twenty11.skysail.common.graphs.Node;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class GraphsModel {

    private ApplicationDescriptor application;

    private Map<String, GraphDetails> graphsMap = new HashMap<String, GraphDetails>();

	private BundleContext context;

    public GraphsModel(BundleContext context, ApplicationDescriptor skysailApp) {
        this.application = skysailApp;
        this.context = context;
        generateModel();
    }

    public GraphsModel() {
        // TODO Auto-generated constructor stub
    }

    private void generateModel() {
        String rootName = application.getApplicationDescription().getName();

        Bundle[] bundles = context.getBundles();
        Bundle bundleToExamine = null;
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals("skysail.common.ext.dbviewer")) {
                bundleToExamine = bundle;
                break;
            }
        }
        if (bundleToExamine == null) {
            return;
        }

        Enumeration<URL> classes = bundleToExamine.findEntries("/", "*.class", true);
        while (classes.hasMoreElements()) {
            try {
                URL nextElement = classes.nextElement();
                InputStream inputStream = nextElement.openConnection().getInputStream();
                DataInputStream dstream = new DataInputStream(new BufferedInputStream(inputStream));
                ClassFile cf = new ClassFile(dstream);
                examineClassFile(cf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

	public void examineClassFile(ClassFile cf) {
        AnnotationsAttribute typeAnnotationsAttribute = (AnnotationsAttribute) cf
                .getAttribute(AnnotationsAttribute.visibleTag);
        if (typeAnnotationsAttribute != null) {
            Annotation nodeAnnotation = typeAnnotationsAttribute.getAnnotation(Node.class.getName());
            if (nodeAnnotation == null) {
                return;
            }

            MemberValue formNameMemberValue = nodeAnnotation.getMemberValue("label");
            GraphDetails form = new GraphDetails(formNameMemberValue.toString());
            graphsMap.put(formNameMemberValue.toString(), form);

            List fields = examineFields(cf, form);
        }

		
	}

	private List examineFields(ClassFile cf,
			GraphDetails form) {
		List fields = cf.getFields();
		for (Object field : fields) {
		    if (field instanceof FieldInfo) {
		        FieldInfo fi = (FieldInfo) field;
		        AttributeInfo attribute = fi.getAttribute(AnnotationsAttribute.visibleTag);
		        if (attribute != null) {
		            AnnotationsAttribute annotationAttribute = (AnnotationsAttribute) attribute;
		            Annotation[] annotations = annotationAttribute.getAnnotations();
		            for (Annotation annotation : annotations) {
		                if (annotation.getTypeName().equals(Field.class.getName())) {
		                    createFieldDetails(form, annotation, fi.getName());
		                }
		            }
		        }
		    }
		}
		return fields;
	}

    private void createFieldDetails(GraphDetails graph, Annotation annotation, String fieldName) {
        @SuppressWarnings("unchecked")
        Set<String> memberNames = annotation.getMemberNames();
//        for (String memberName : memberNames) {
//            FieldDetails fieldDetails = new FieldDetails(fieldName);
//            MemberValue memberValue = annotation.getMemberValue(memberName);
//            if ("valuesProvider".equals(memberName)) {
//                ClassMemberValue cmv = (ClassMemberValue) memberValue;
//                String valueProvider = cmv.getValue(); // de.twenty11.skysail.common.forms.ValuesProviderImpl
//                Object foundClass = findClass(valueProvider, context);
//                if (foundClass != null) {
//                    ValuesProvider valuesProvider = (ValuesProvider)foundClass;
//                    fieldDetails.setValidValues(valuesProvider.getValues());
//                }
//            }
//
//            graph.addField(fieldDetails);
//        }
    }

    private Object findClass(String valueProvider) {
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            try {
                Class<?> loadedClass = bundle.loadClass(valueProvider);
                return loadedClass.newInstance();
            } catch (Exception e) {
                /** it's ok, really */
                continue;
            }
        }
        return null;
    }

    public List<GraphDetails> getAllGraphs() {
        return new ArrayList<GraphDetails>(graphsMap.values());
    }

}
