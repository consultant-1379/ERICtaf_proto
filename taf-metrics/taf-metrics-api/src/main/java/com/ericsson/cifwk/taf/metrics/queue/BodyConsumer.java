package com.ericsson.cifwk.taf.metrics.queue;

public interface BodyConsumer {
    void handle(byte[] body);
}
