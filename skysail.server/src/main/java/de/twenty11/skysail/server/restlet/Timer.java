package de.twenty11.skysail.server.restlet;

import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.routing.Filter;

public class Timer extends Filter {

	public static final String CONTEXT_EXECUTION_STARTED = "de.twenty11.syksail.server.restlet.Time.executionStarted";
	private long started;

	public Timer(Context context) {
		super(context);
	}

	@Override
	protected int beforeHandle(Request request, Response response) {
		started = System.nanoTime();
		if (getContext() != null) {
			getContext().getAttributes().put(CONTEXT_EXECUTION_STARTED, started);
		}
		return Filter.CONTINUE;
	}

	@Override
	protected void afterHandle(Request request, Response response) {
		// the execution duration transmitted in the response is calculated at
		// the last possible
		// moment before the response is created; here it is already to late and
		// we can only log
		// the time.
		getLogger().log(
				Level.FINEST,
				"Complete execution time for request: "
						+ (System.nanoTime() - started) + "ns");
	}

}
