package com.ericsson.cifwk.taf.grid.runner;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
public final class MainRunner implements TestRunner {

    private final Logger logger = LoggerFactory.getLogger(MainRunner.class);

    private Method method;
    private String[] args;
    private TestContainer container;
    private Map<String, String> attributes;

    @Override
    public String getName() {
        return "main";
    }

    @Override
    public void setUp(TestContainer container, String classpath, Map<String, String> attributes) {
        this.container = container;
        this.attributes = attributes;

        String className = attributes.get("className");
        args = attributes.get("args").split(";");

        container.init(classpath);
        Object instance = container.createInstance(className);

        try {
            method = instance.getClass().getMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void tearDown() {
        container.destroy();
    }

    @Override
    public void runTest() {
        try {
            method.invoke(null, (Object) args);
        } catch (Exception e) {
            logger.warn("Failed to execute test class", e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String getTestName() {
        String className = attributes.get("className");
        return className + ".main()";
    }


}
