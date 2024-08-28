package com.ericsson.cifwk.taf.grid.runner;

/**
 *
 */
public interface TestContainer extends NamedService {

    void init(String testware);

    Object createInstance(String className);

    void destroy();

}
