package de.twenty11.skysail.server.utils;


public class IOUtils {

    public static String convertStreamToString(java.io.InputStream is) {
        if(is == null) {
        	return null;
        }
        java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        s.close();
        return result;
    }

}
