//package de.twenty11.skysail.server.graphs.internal;
//
//import java.io.BufferedInputStream;
//import java.io.DataInputStream;
//import java.io.InputStream;
//import java.net.URL;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javassist.bytecode.AnnotationsAttribute;
//import javassist.bytecode.ClassFile;
//import javassist.bytecode.annotation.Annotation;
//import javassist.bytecode.annotation.MemberValue;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//import org.osgi.framework.Bundle;
//import org.osgi.framework.BundleContext;
//import org.restlet.data.ChallengeResponse;
//import org.restlet.representation.Representation;
//import org.restlet.resource.ClientResource;
//
//import de.twenty11.skysail.common.graphs.Graph;
//import de.twenty11.skysail.common.graphs.GraphDetails;
//import de.twenty11.skysail.common.graphs.GraphProvider;
//import de.twenty11.skysail.common.graphs.NodeProvider;
//import de.twenty11.skysail.common.responses.Response;
//import de.twenty11.skysail.server.services.ApplicationDescriptor;
//
//public class GraphsModel {
//
//    private ApplicationDescriptor application;
//
//    private Map<String, GraphDetails> graphsMap = new HashMap<String, GraphDetails>();
//
//    /** deals with json objects */
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    private BundleContext context;
//
//    public GraphsModel(BundleContext context, ApplicationDescriptor skysailApp, ChallengeResponse challengeResponse) {
//        this.application = skysailApp;
//        this.context = context;
//        generateModel(challengeResponse);
//    }
//
//    public GraphsModel() {
//        // TODO Auto-generated constructor stub
//    }
//
//    private void generateModel(ChallengeResponse challengeResponse) {
//        String rootName = application.getApplicationDescription().getName();
//
//        Bundle[] bundles = context.getBundles();
//        Bundle bundleToExamine = null;
//        for (Bundle bundle : bundles) {
//            if (bundle.getSymbolicName().equals("skysail.server.ext.osgimonitor")) {
//                bundleToExamine = bundle;
//                break;
//            }
//        }
//        if (bundleToExamine == null) {
//            return;
//        }
//
//        Enumeration<URL> classes = bundleToExamine.findEntries("/", "*.class", true);
//        while (classes.hasMoreElements()) {
//            try {
//                URL nextElement = classes.nextElement();
//                InputStream inputStream = nextElement.openConnection().getInputStream();
//                DataInputStream dstream = new DataInputStream(new BufferedInputStream(inputStream));
//                ClassFile cf = new ClassFile(dstream);
//                examineClassFile(cf, challengeResponse);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//    public void examineClassFile(ClassFile cf, ChallengeResponse challengeResponse) {
//        Annotation nodeAnnotation = getAnnotation(cf, Graph.class);
//        if (nodeAnnotation == null) {
//            return;
//        }
//
//        MemberValue memberValue = nodeAnnotation.getMemberValue("nodesPath");
//        if (memberValue == null) {
//            return;
//        }
//
//        //ClientResource columns = new ClientResource("riap://component" + memberValue.toString().replace("\"", ""));
//        ClientResource columns = new ClientResource("http://localhost:8554" + memberValue.toString().replace("\"", ""));
//        columns.setChallengeResponse(challengeResponse);
//        Representation representation = columns.get();
//        try {
//            Response<List<? extends NodeProvider>> response = mapper.readValue(representation.getText(),
//                    new TypeReference<Response<List<? extends NodeProvider>>>() {
//                    });
//            List<? extends NodeProvider> payload = response.getData();
//            
//            
//            
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//    }
//
//    private Annotation getAnnotation(ClassFile cf, Class<?> annotation) {
//        AnnotationsAttribute attribute = (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag);
//
//        if (attribute == null) {
//            return null;
//        }
//        Annotation nodeAnnotation = attribute.getAnnotation(annotation.getName());
//        if (nodeAnnotation == null) {
//            return null;
//        }
//        return nodeAnnotation;
//    }
//
//}
