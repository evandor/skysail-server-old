package de.twenty11.skysail.server.restlet;

public enum ParameterStyle {

    HEADER, MATRIX, PLAIN, QUERY, TEMPLATE;

    @Override
    public String toString() {
        String result = null;
        if (equals(HEADER)) {
            result = "header";
        } else if (equals(MATRIX)) {
            result = "matrix";
        } else if (equals(PLAIN)) {
            result = "plain";
        } else if (equals(QUERY)) {
            result = "query";
        } else if (equals(TEMPLATE)) {
            result = "template";
        }

        return result;
    }

}