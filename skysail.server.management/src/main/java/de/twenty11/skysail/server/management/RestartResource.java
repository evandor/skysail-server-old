package de.twenty11.skysail.server.management;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class RestartResource extends ServerResource {

	/** slf4j based logger implementation */
	private static Logger logger = LoggerFactory
			.getLogger(RestartResource.class);
	private String resourceRef;

	/** deals with json objects */
	private final ObjectMapper mapper = new ObjectMapper();

	public RestartResource() {
		// setName("generic graph representation resource");
		// setDescription("The graph representation for the referenced resource");
	}

	@Override
	protected void doInit() throws ResourceException {
		// // TODO improve
		// resourceRef =
		// getRequest().getResourceRef().getParentRef().toString();
		// // strip last slash
		// resourceRef = resourceRef.substring(0, resourceRef.length() - 1);
	}

	@Get
	public StringRepresentation restart() {
		// final String javaBin = System.getProperty("java.home") +
		// File.separator + "bin" + File.separator + "java";

		final ArrayList<String> command = new ArrayList<String>();
		command.add("/home/carsten/git/skysail-server-ext/skysail.server.ext.osgimonitor/etc/pax-runner/local.sh");

		final ProcessBuilder builder = new ProcessBuilder(command);
		try {
			builder.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new StringRepresentation("problem restarting...");
		}
		System.exit(0);
		return new StringRepresentation("restarting...");

	}

	/**
	 * Sun property pointing the main class and its arguments. Might not be
	 * defined on non Hotspot VM implementations.
	 */
	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	/**
	 * Restart the current Java application
	 * 
	 * @param runBeforeRestart
	 *            some custom code to be run before restarting
	 * @throws IOException
	 */
	public static void restartApplication(Runnable runBeforeRestart)
			throws IOException {
		try {
			final StringBuffer cmd = calcJavaCommand();
			
			// execute the command in a shutdown hook, to be sure that all the
			// resources have been disposed before restarting the application
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						Runtime.getRuntime().exec(cmd.toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			// execute some custom code before restarting
			if (runBeforeRestart != null) {
				runBeforeRestart.run();
			}
			// exit
			System.exit(0);
		} catch (Exception e) {
			// something went wrong
			throw new IOException(
					"Error while trying to restart the application", e);
		}
	}

	private static StringBuffer calcJavaCommand() {
		System.getProperties();
		// java binary
		String java = System.getProperty("java.home") + "/bin/java";
		// vm arguments
		List<String> vmArguments = ManagementFactory.getRuntimeMXBean()
				.getInputArguments();
		StringBuffer vmArgsOneLine = new StringBuffer();
		for (String arg : vmArguments) {
			// if it's the agent argument : we ignore it otherwise the
			// address of the old application and the new one will be in
			// conflict
			if (!arg.contains("-agentlib")) {
				vmArgsOneLine.append(arg);
				vmArgsOneLine.append(" ");
			}
		}
		// init the command to execute, add the vm args
		final StringBuffer cmd = new StringBuffer("\"" + java + "\" "
				+ vmArgsOneLine);

		// program main and program arguments
		String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(
				" ");
		// program main is a jar
		if (mainCommand[0].endsWith(".jar")) {
			// if it's a jar, add -jar mainJar
			cmd.append("-jar " + new File(mainCommand[0]).getPath());
		} else {
			// else it's a .class, add the classpath and mainClass
			cmd.append("-cp \"" + System.getProperty("java.class.path")
					+ "\" " + mainCommand[0]);
		}
		// finally add program arguments
		for (int i = 1; i < mainCommand.length; i++) {
			cmd.append(" ");
			cmd.append(mainCommand[i]);
		}
		return cmd;
	}

	public static void main(String[] args) {
		try {
			new RestartResource().restartApplication(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
