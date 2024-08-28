package com.ericsson.cifwk.taf.metrics.sample;

import java.io.Closeable;
import java.io.IOException;

public interface Dumpling extends Closeable {
    void initialize() throws IOException;

    void write(Sample sample) throws IOException;

    void flush() throws IOException;
}
