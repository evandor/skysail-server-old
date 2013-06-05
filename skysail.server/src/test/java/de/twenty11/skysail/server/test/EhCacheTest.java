package de.twenty11.skysail.server.test;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import org.junit.Test;

public class EhCacheTest {

    @Test
    public void test() {
        CacheManager manager = CacheManager.newInstance();
        manager.addCache("testCache");
        Cache cache = manager.getCache("testCache");
        CacheConfiguration config = cache.getCacheConfiguration();
        config.setTimeToIdleSeconds(60);
        config.setTimeToLiveSeconds(120);
        config.setMaxEntriesLocalHeap(100);
        config.setMaxEntriesLocalDisk(100);
        Element element = new Element("key1", "value1");
        cache.put(element);
        int elementsInMemory = cache.getSize();
    }
}
