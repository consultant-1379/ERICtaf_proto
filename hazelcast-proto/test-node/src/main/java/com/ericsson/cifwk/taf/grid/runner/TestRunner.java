package com.ericsson.cifwk.taf.grid.runner;

import java.util.Map;

/**
 *
 */
public interface TestRunner extends NamedService {

    void setUp(TestContainer container, String classpath, Map<String, String> attributes);

    void runTest();

    void tearDown();

    String getTestName();

}
