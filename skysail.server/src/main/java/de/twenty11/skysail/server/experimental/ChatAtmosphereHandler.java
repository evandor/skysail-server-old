//package de.twenty11.skysail.server.experimental;
//
//import java.io.IOException;
//import java.util.Date;
//
//import org.atmosphere.config.service.ManagedService;
//import org.atmosphere.config.service.Message;
//import org.codehaus.jackson.map.ObjectMapper;
//
//@ManagedService(path = "/chat")
//public class ChatAtmosphereHandler {
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Message
//    public String onMessage(String message) throws IOException {
//        return mapper.writeValueAsString(mapper.readValue(message, Data.class));
//    }
//
//    public final static class Data {
//
//        private String message;
//        private String author;
//        private long time;
//
//        public Data() {
//            this("", "");
//        }
//
//        public Data(String author, String message) {
//            this.author = author;
//            this.message = message;
//            this.time = new Date().getTime();
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public String getAuthor() {
//            return author;
//        }
//
//        public void setAuthor(String author) {
//            this.author = author;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//
//        public long getTime() {
//            return time;
//        }
//
//        public void setTime(long time) {
//            this.time = time;
//        }
//
//    }
// }
