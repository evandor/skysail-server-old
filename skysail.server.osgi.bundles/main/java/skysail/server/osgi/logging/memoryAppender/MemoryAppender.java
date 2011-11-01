package skysail.server.osgi.logging.memoryAppender;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class MemoryAppender extends AppenderBase<ILoggingEvent> {

    private static HashMap<String, LimitedLinkedHashMap<Integer, ILoggingEvent>> loggerMaps = new LinkedHashMap<String, LimitedLinkedHashMap<Integer, ILoggingEvent>>();

    private static Logger logger = LoggerFactory.getLogger(MemoryAppender.class);

    public MemoryAppender() {
        logger.info("new MemoryAppender created");
    }
    
    @Override
    protected void append(ILoggingEvent event) {
        String bundleName = event.getLoggerName();
        LimitedLinkedHashMap<Integer, ILoggingEvent> bundleLog = null;
        if (loggerMaps.containsKey(bundleName)) {
            bundleLog = loggerMaps.get(bundleName);
        } else {
            bundleLog = new LimitedLinkedHashMap<Integer, ILoggingEvent>(10);
            loggerMaps.put(bundleName, bundleLog);
        }
        bundleLog.put(bundleLog.size(), event);
    }

    public LimitedLinkedHashMap<Integer, ILoggingEvent> getLogForBundle(final String bundleName) {
        return loggerMaps.get(bundleName);
    }
    
    public static Set<String> getLogNames () {
        return loggerMaps.keySet();                
    }
    
    public static Set<String> getLogs(final String logname) {
        if (!loggerMaps.containsKey(logname)) {
            return Collections.emptySet();
        }
        Set<String> result = new HashSet<String>();
        LimitedLinkedHashMap<Integer, ILoggingEvent> limitedLinkedHashMap = loggerMaps.get(logname);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        for (Integer nr : limitedLinkedHashMap.keySet()) {
            ILoggingEvent e = limitedLinkedHashMap.get(nr);
            String line = new StringBuffer().append(nr + " [").append(sdf.format(new Date(e.getTimeStamp()))).append("]: ").append(e.toString()).toString();
            result.add(line);
        }
        return result;
    }

}
