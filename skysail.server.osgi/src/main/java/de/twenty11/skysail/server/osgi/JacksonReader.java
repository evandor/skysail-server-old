package de.twenty11.skysail.server.osgi;
//package de.twenty11.skysail.server.osgi;
//
//import java.io.Serializable;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.type.TypeReference;
//
//import de.skysail.common.skysailRequest;
//
//
//public class JacksonReader<T extends Serializable> {
//
//	//http://stackoverflow.com/questions/3907929/should-i-make-jacksons-objectmapper-as-static-final
//	private static final ObjectMapper mapper = new ObjectMapper();
//	
//	public T read(String serializedEntity) {
//		
//		skysailRequest<T> request;
//		try {
//			request = mapper.readValue(serializedEntity, 
//					new TypeReference<skysailRequest<T>>(){});
//			return request.getData();
//		} catch (Exception e) {
//			throw new RuntimeException("problem deserializing entity", e);
//		}
//	}
//
//}
