package com.ericsson.cifwk.taf.grid.runner;

import java.util.ServiceLoader;

/**
 *
 */
public final class SpiRegistry {

    public TestContainer findContainer(String name) {
        ServiceLoader<TestContainer> containers = ServiceLoader.load(TestContainer.class);
        for (TestContainer container : containers) {
            if (container.getName().equalsIgnoreCase(name)) {
                return container;
            }
        }
        return new IsolatedContainer();
    }

    public TestRunner findRunner(String name) {
        ServiceLoader<TestRunner> runners = ServiceLoader.load(TestRunner.class);
        for (TestRunner runner : runners) {
            if (runner.getName().equalsIgnoreCase(name)) {
                return runner;
            }
        }
        return new ClassRunner();
    }

}
