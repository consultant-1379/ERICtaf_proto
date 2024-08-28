package com.ericsson.cifwk.dashboard.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Queue;

public class QueueManager<T> {

    private final Cache<Queue<T>, Boolean> queues;

    public QueueManager() {
        queues = CacheBuilder.newBuilder().weakKeys().build();
    }

    public void add(Queue<T> queue) {
        queues.put(queue, true);
    }

    public void remove(Queue<T> queue) {
        queues.invalidate(queue);
    }

    public void offerToAll(T object) {
        for (Queue<T> queue : queues.asMap().keySet()) {
            queue.offer(object);
        }
    }

    public long size() {
        return queues.size();
    }
}
