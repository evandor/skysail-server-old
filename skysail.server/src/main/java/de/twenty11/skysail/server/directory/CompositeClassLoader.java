package de.twenty11.skysail.server.directory;

import java.net.URL;
import java.util.Vector;

public class CompositeClassLoader extends ClassLoader {

    private Vector<ClassLoader> classLoaders = new Vector<ClassLoader>();

    @Override
    public URL getResource(String name) {
        for (ClassLoader cl : classLoaders) {
            URL resource = cl.getResource(name);
            if (resource != null) {
                return resource;
            }
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        for (ClassLoader cl : classLoaders) {
            try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException ex) {
            }
        }
        throw new ClassNotFoundException(name);

    }

    public void addClassLoader(ClassLoader cl) {
        classLoaders.add(cl);
    }

}
