package skysail.server.osgi.logging.memoryAppender;

import java.util.LinkedHashMap;

public class LimitedLinkedHashMap<M, N> extends LinkedHashMap<M, N> {

    /**
     * 
     */
    private static final long serialVersionUID = 343619147276712331L;
    
    private int maxSizeOfMap;

    public LimitedLinkedHashMap(final int maxSize) {
        this.maxSizeOfMap = maxSize;
    }
    
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
        return this.size() > maxSizeOfMap;   
    }
}
