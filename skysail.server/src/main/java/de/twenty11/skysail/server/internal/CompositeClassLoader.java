///**
// *  Copyright 2011 Carsten Gräf
// *
// *  Licensed under the Apache License, Version 2.0 (the "License");
// *  you may not use this file except in compliance with the License.
// *  You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// *  Unless required by applicable law or agreed to in writing, software
// *  distributed under the License is distributed on an "AS IS" BASIS,
// *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *  See the License for the specific language governing permissions and
// *  limitations under the License.
// * 
// */
//
//package de.twenty11.skysail.server.internal;
//
//import java.net.URL;
//import java.util.Vector;
//
///**
// * checking idea from
// * http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
// * 
// */
//public class CompositeClassLoader extends ClassLoader {
//    private Vector<ClassLoader> classLoaders = new Vector<ClassLoader>();
//
//    @Override
//    public URL getResource(String name) {
//        for (ClassLoader cl : classLoaders) {
//
//            URL resource = cl.getResource(name);
//            if (resource != null)
//                return resource;
//
//        }
//
//        return null;
//    }
//
//    @Override
//    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//
//        for (ClassLoader cl : classLoaders) {
//            try {
//                return cl.loadClass(name);
//            } catch (ClassNotFoundException ex) {
//
//            }
//        }
//
//        throw new ClassNotFoundException(name);
//    }
//
//    public void addClassLoader(ClassLoader cl) {
//        classLoaders.add(cl);
//    }
// }
