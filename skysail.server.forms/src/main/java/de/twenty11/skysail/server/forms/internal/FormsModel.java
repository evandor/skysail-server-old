package de.twenty11.skysail.server.forms.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import de.twenty11.skysail.common.forms.FieldDetails;
import de.twenty11.skysail.common.forms.Form;
import de.twenty11.skysail.common.forms.FormDetails;
import de.twenty11.skysail.server.services.ApplicationDescriptor;

public class FormsModel {

    private ApplicationDescriptor application;

    private ClassPool pool = ClassPool.getDefault();

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
                    formsMap .put(formNameMemberValue.toString(), form);

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
                                        Set<String> memberNames = annotation.getMemberNames();
                                        for (String object : memberNames) {
                                            MemberValue memberValue = annotation.getMemberValue(object);
                                            System.out.println("@" + memberValue);
                                            FieldDetails fieldDetails = new FieldDetails(memberValue.toString());
                                            form.addField(fieldDetails);
                                        }
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
