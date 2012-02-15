/**
 *  Copyright 2011 Carsten Gräf
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * 
 */

package de.twenty11.skysail.logging.startup;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


/**
 * someone wants to know, if a bundle, identified by its name, and (somehow) providing a list of 
 * expected and unexpected log pattern - based on those patterns and the actual log - was started 
 * successfully.
 * 
 * @author carsten
 *
 */
public interface StartupLogProviderService {
    
    /**
     * @return
     */
    Set<String> getBundleNames();
    
    /**
     * @param bundle
     * @param expected
     * @param unexpected
     * @return
     */
    Status getStartupStatus(String bundle, List<Pattern> expected, List<Pattern> unexpected);

}
