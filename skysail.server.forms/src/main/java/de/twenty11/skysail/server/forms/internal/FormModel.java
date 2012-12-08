package de.twenty11.skysail.server.forms.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import de.twenty11.skysail.common.forms.Field;
import de.twenty11.skysail.server.services.ApplicationService;

public class FormModel {

    private ApplicationService application;

    private ClassPool pool = ClassPool.getDefault();

    public FormModel(BundleContext context, ApplicationService skysailApp) {
        this.application = skysailApp;
        generateModel(context);
    }

    private void generateModel(BundleContext context) {
        String rootName = application.getApplication().getName();

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
                cf.getAttributes();
                List fields = cf.getFields();

                for (Object field : fields) {
                    if (field instanceof FieldInfo) {
                        FieldInfo fi = (FieldInfo) field;
                        // AttributeInfo attribute = ((FieldInfo)field).getAttribute(AnnotationsAttribute.visibleTag);
                        AttributeInfo attribute = fi.getAttribute(AnnotationsAttribute.visibleTag);
                        // Object annotation = field.getAnnotation(Field.class);
                        if (attribute != null) {
                            AnnotationsAttribute annotationAttribute = (AnnotationsAttribute) attribute;
                            Annotation[] annotations = annotationAttribute.getAnnotations();
                            for (Annotation annotation : annotations) {
                                if (annotation.getTypeName().equals(Field.class.getName())) {
                                    Set<String> memberNames = annotation.getMemberNames();
                                    for (String object : memberNames) {
                                        MemberValue memberValue = annotation.getMemberValue(object);
                                        System.out.println("@" + memberValue);
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

}
