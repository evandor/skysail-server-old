package skysail.server.osgi.logging.memoryAppender;

import java.util.Set;

public class MemoryLogProviderImpl implements MemoryLogProvider {

    @Override
    public Set<String> getLoggers() {
        return MemoryAppender.getLogNames();
    }

    @Override
    public Set<String> getLogs(String logname) {
        return MemoryAppender.getLogs(logname);
    }

}
