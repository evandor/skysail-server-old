package de.twenty11.skysail.server.forms.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

import javassist.ClassPool;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.MemberValue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.common.forms.FieldDetails;
import de.twenty11.skysail.common.forms.Form;
import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.common.forms.ValuesProvider;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class FormsModel {

    private ApplicationDescriptor application;

    private Map<String, FormDetails> formsMap = new HashMap<String, FormDetails>();

    public FormsModel(BundleContext context, ApplicationDescriptor skysailApp) {
        this.application = skysailApp;
        generateModel(context);
    }

    public FormsModel() {
        // TODO Auto-generated constructor stub
    }

    private void generateModel(BundleContext context) {
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

                AnnotationsAttribute typeAnnotationsAttribute = (AnnotationsAttribute) cf
                        .getAttribute(AnnotationsAttribute.visibleTag);
                if (typeAnnotationsAttribute != null) {
                    Annotation formAnnotation = typeAnnotationsAttribute.getAnnotation(Form.class.getName());
                    if (formAnnotation == null) {
                        continue;
                    }

                    MemberValue formNameMemberValue = formAnnotation.getMemberValue("name");
                    FormDetails form = new FormDetails(formNameMemberValue.toString());
                    formsMap.put(formNameMemberValue.toString(), form);

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
                                        createFieldDetails(form, annotation, context, fi.getName());
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void createFieldDetails(FormDetails form, Annotation annotation, BundleContext context, String fieldName) {
        @SuppressWarnings("unchecked")
        Set<String> memberNames = annotation.getMemberNames();
        for (String memberName : memberNames) {
            FieldDetails fieldDetails = new FieldDetails(fieldName);
            MemberValue memberValue = annotation.getMemberValue(memberName);
            if ("valuesProvider".equals(memberName)) {
                ClassMemberValue cmv = (ClassMemberValue) memberValue;
                String valueProvider = cmv.getValue(); // de.twenty11.skysail.common.forms.ValuesProviderImpl
                Object foundClass = findClass(valueProvider, context);
                if (foundClass != null) {
                    ValuesProvider valuesProvider = (ValuesProvider)foundClass;
                    fieldDetails.setValidValues(valuesProvider.getValues());
                }
            }

            form.addField(fieldDetails);
        }
    }

    private Object findClass(String valueProvider, BundleContext context) {
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

    public List<FormDetails> getAllForms() {
        return new ArrayList<FormDetails>(formsMap.values());
    }

}
