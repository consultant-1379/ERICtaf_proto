package com.ericsson.cifwk.dashboard.util;

import com.google.common.collect.ForwardingQueue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CircularBuffer<T> extends ForwardingQueue<T> {

    private final ConcurrentLinkedDeque<T> deque;
    private final int maxSize;

    @Override
    public boolean add(T element) {
        return circularAdd(element);
    }

    @Override
    public boolean offer(T element) {
        return circularAdd(element);
    }

    private boolean circularAdd(T element) {
        while (deque.size() >= maxSize) {
            deque.pollFirst();
        }
        return deque.add(element);
    }

    public CircularBuffer(int maxSize) {
        super();
        this.maxSize = maxSize;
        deque = new ConcurrentLinkedDeque<>();
    }

    @Override
    protected Queue<T> delegate() {
        return deque;
    }
}
