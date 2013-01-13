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
//import org.restlet.Context;
//import org.restlet.Request;
//import org.restlet.Response;
//import org.restlet.data.Reference;
//import org.restlet.resource.Directory;
//
///**
// * checking idea from
// * http://nexnet.wordpress.com/2010/09/29/clap-protocol-in-restlet-and-osgi/
// * 
// */
//public class ClassLoaderDirectory extends Directory {
//    private ClassLoader classLoader;
//
//    public ClassLoaderDirectory(Context context, Reference rootLocalReference, ClassLoader cl) {
//
//        super(context, rootLocalReference);
//        this.classLoader = cl;
//    }
//
//    @Override
//    public void handle(Request request, Response response) {
//        final ClassLoader saveCL = Thread.currentThread().getContextClassLoader();
//        Thread.currentThread().setContextClassLoader(classLoader);
//        super.handle(request, response);
//        Thread.currentThread().setContextClassLoader(saveCL);
//    }
// }
