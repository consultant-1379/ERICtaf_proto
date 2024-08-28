package com.ericsson.cifwk.taf.grid.runner;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
public final class ClassRunner implements TestRunner {

    private final Logger logger = LoggerFactory.getLogger(ClassRunner.class);

    private Object instance;
    private Method method;
    private TestContainer container;
    private Map<String, String> attributes;

    @Override
    public String getName() {
        return "class";
    }

    @Override
    public void setUp(TestContainer container, String classpath, Map<String, String> attributes) {
        this.container = container;
        this.attributes = attributes;

        String className = attributes.get("className");
        String methodName = attributes.get("methodName");

        container.init(classpath);
        instance = container.createInstance(className);

        try {
            method = instance.getClass().getMethod(methodName);
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
            method.invoke(instance);
        } catch (Exception e) {
            logger.warn("Failed to execute test class", e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public String getTestName() {
        String className = attributes.get("className");
        String methodName = attributes.get("methodName");
        return className + "." + methodName + "()";
    }

}
