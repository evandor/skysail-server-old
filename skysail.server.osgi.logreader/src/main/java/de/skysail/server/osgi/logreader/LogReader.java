package de.skysail.server.osgi.logreader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import skysail.server.osgi.logging.memoryAppender.MemoryLogProvider;
import de.twenty11.skysail.common.messages.LinkData;
import de.twenty11.skysail.server.UrlMapper;

public class LogReader implements UrlMapper {

    private static MemoryLogProvider logProvider;
    
    @Override
    public Map<String, String> getUrlMapping() {
        Map<String,String> queue = Collections.synchronizedMap(new LinkedHashMap<String,String>());
        queue.put("/logs/", LogsResource.class.getName());
        queue.put("/logs/{logname}", LogResource.class.getName());
        return queue;
    }

    public List<LinkData> getLoggerKeys() {
        List<LinkData> links = new ArrayList<LinkData>();
        Set<String> loggers = logProvider.getLoggers();
        for (String loggerName : loggers) {
            LinkData link = new LinkData();
            link.setHref("logs/" + loggerName);
            link.setValue(loggerName);
            links.add(link);
        }
        Collections.sort(links);
        return links;
    }
    
    public void setLogProvider(MemoryLogProvider logProvider) {
        LogReader.logProvider = logProvider;
    }

    public List<LinkData> getLogs(String logname) {
        Set<String> logs = logProvider.getLogs(logname);
        List<LinkData> links = new ArrayList<LinkData>();
        for (String line : logs) {
            LinkData link = new LinkData();
            link.setHref("logs/" + line);
            link.setValue(line);
            links.add(link);
        }
        //Collections.sort(links);
        return links;
    }

}
