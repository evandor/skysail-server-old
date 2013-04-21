package de.twenty11.skysail.server.descriptors;

public class FileDescriptor {

    private String content;
    private String extention;

    public FileDescriptor(String content, String extension) {
        this.content = content;
        this.extention = extension;
    }

    public String getContent() {
        return content;
    }

    public String getExtention() {
        return extention;
    }

}
