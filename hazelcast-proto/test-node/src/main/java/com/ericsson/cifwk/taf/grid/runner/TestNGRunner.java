package com.ericsson.cifwk.taf.grid.runner;

import java.util.Map;

/**
 *
 */
public final class TestNGRunner implements TestRunner {

    @Override
    public void setUp(TestContainer container, String classpath, Map<String, String> attributes) {
    }

    @Override
    public void tearDown() {
    }

    @Override
    public String getName() {
        return "testNg";
    }

    @Override
    public void runTest() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTestName() {
        return "TestNG";
    }

}
