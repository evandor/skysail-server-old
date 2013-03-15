package de.twenty11.skysail.server.internal;

import java.util.Collection;
import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {

    private static final long serialVersionUID = -8302207625132859978L;

    private final int limit;

    public LimitedQueue(int i) {
        this.limit = i;
    }

    @Override
    public boolean add(E e) {
        super.add(e);
        if (size() > limit) {
            super.remove();
        }
        return true;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("not applicable for LimitedQueue");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("not applicable for LimitedQueue");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("not applicable for LimitedQueue");
    }

    @Override
    public void addFirst(E e) {
        throw new UnsupportedOperationException("not applicable for LimitedQueue");
    }

    @Override
    public void addLast(E e) {
        throw new UnsupportedOperationException("not applicable for LimitedQueue");
    }
}
