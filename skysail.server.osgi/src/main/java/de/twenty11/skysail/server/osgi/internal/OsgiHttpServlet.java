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

package de.twenty11.skysail.server.osgi.internal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class OsgiHttpServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683923370262781874L;

	/**
	 * will return "components" assuming path looks like
	 * "/components/<component>/<command>/".
	 * 
	 * @param pathInfo
	 * @return components part or null if not able to determine
	 */
	protected String getComponentFromPath(String pathInfo) {
		if (pathInfo == null)
			return null;
		String[] parts = pathInfo.split("/");
		if (parts != null && parts.length >= 3)
			return parts[2];
		return null;
	}

	/**
	 * will return "command" assuming path looks like
	 * "/components/<component>/<command>/".
	 * 
	 * @param pathInfo
	 * @return command part or null if not able to determine
	 */
	protected String getCommandFromPath(String pathInfo) {
		if (pathInfo == null)
			return null;
		String[] parts = pathInfo.split("/");
		if (parts != null && parts.length >= 4)
			return parts[3];
		return null;
	}

	protected byte[] serialize(Object components) {

		try {
			// serialize - the recipient might not have the same classloader
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(components);
			out.close();

			return bos.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException ("could not serialize object", e);
		}
	}

	protected void serializeAndAddToRequest(HttpServletRequest request,
			String pathInfo, Object data) throws IOException {
		byte[] ba = serialize(data);
		request.setAttribute("pathInfo", pathInfo);
		request.setAttribute("payload", ba);
	}

	protected String getQueryFromPath(String pathInfo) {
		// assume path looks like "/components/command/something/";
		String[] parts = pathInfo.split("/");
		StringBuffer sb = new StringBuffer();
		for (int i = 4; i < parts.length; i++) {
			sb.append("/").append(parts[i]);
		}
		return sb.toString();
	}

}
