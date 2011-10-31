package skysail.server.osgi.logging.memoryAppender;

import java.util.Set;

public interface MemoryLogProvider {
    
    Set<String> getLoggers();

    Set<String> getLogs(String logname);

}
