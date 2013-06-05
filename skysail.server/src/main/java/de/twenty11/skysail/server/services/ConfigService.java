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
//package de.twenty11.skysail.server.services;
//
//import java.util.Properties;
//
///**
// * Defines a configuration service which will return values (strings, properties, ...) for a given
// * identifier.
// * 
// * @author carsten
// *
// */
//public interface ConfigService {
//
//    /**
//     * looks for identifier and returns the value.
//     * @param identifier key
//     * @return the value for the identifier, null if not found
//     */
//    String getString(String identifier);
//    
//    /**
//     * looks for identifier and returns the value.
//     * @param identifier key
//     * @param defaultValue the value to use if key is not found
//     * @return the value for the identifier
//     */
//    String getString(String identifier, String defaultValue);
//
//    /**
//     * return properties for given identifier.
//     * 
//     * @param identifier the key for the properties
//     * @return a properties object containing the keys/values for the given identifier
//     */
//    Properties getProperties(String identifier);
//    
// }
