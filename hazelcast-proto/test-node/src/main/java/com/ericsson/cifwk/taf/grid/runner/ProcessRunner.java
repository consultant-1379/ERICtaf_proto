package com.ericsson.cifwk.taf.grid.runner;

import com.google.common.base.Throwables;

import java.io.IOException;
import java.util.Map;

public class ProcessRunner implements TestRunner {

    String cmd;

    @Override
    public void setUp(TestContainer container, String classpath, Map<String, String> attributes) {
        cmd = attributes.get("cmd");
    }

    @Override
    public void runTest() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(cmd);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void tearDown() {
    }

    @Override
    public String getTestName() {
        return cmd;
    }

    @Override
    public String getName() {
        return "process";
    }

}
