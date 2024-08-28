package com.ericsson.cifwk.taf.grid.sample;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class CalculatingTestCase {

    private final AtomicInteger counter = new AtomicInteger();

    public void doNothing() {
        counter.incrementAndGet();
    }

}
