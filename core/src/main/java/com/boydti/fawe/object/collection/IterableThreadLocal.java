package com.boydti.fawe.object.collection;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class IterableThreadLocal<T> extends ThreadLocal<T> implements Iterable<T>, AutoCloseable {
    private final ThreadLocal<Boolean> initialized = new ThreadLocal<>();
    private final ConcurrentLinkedDeque<T> allValues = new ConcurrentLinkedDeque<>();

    public IterableThreadLocal() {
    }

    @Override
    protected final T initialValue() {
        T value = init();
        if (value != null) {
            allValues.add(value);
        }
        initialized.set(true);
        return value;
    }

    @Override
    public final Iterator<T> iterator() {
        return getAll().iterator();
    }

    public T init() {
        return null;
    }

    public void clean() {
        if (Boolean.TRUE.equals(initialized.get())) {
            T value = get();
            if (value != null) {
                allValues.remove(value);
            }
        }
        initialized.remove();
        remove();
    }

    public static void clean(ThreadLocal<?> instance) {
        if (instance instanceof IterableThreadLocal) {
            ((IterableThreadLocal<?>) instance).clean();
        } else {
            instance.remove();
        }
    }

    public final Collection<T> getAll() {
        return Collections.unmodifiableCollection(allValues);
    }

    @Override
    public void close() {
        clean();
    }
}
