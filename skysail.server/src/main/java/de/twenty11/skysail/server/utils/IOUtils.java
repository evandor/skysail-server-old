package de.twenty11.skysail.server.utils;


public class IOUtils {

    public static String convertStreamToString(java.io.InputStream is) {
        // StringWriter writer = new StringWriter();
        // try {
        // org.apache.commons.io.IOUtils.copy(is, writer, "UTF-8");
        // return writer.toString();
        // } catch (IOException e) {
        // throw new RuntimeException("problem reading input stream", e);
        // }
        //
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
