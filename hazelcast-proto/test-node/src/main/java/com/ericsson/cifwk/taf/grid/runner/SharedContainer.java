package com.ericsson.cifwk.taf.grid.runner;

import com.google.common.base.Throwables;

/**
 *
 */
public final class SharedContainer implements TestContainer {

    @Override
    public String getName() {
        return "shared";
    }

    @Override
    public void init(String testware) {
    }

    @Override
    public Object createInstance(String className) {
        ClassLoader classLoader = getClass().getClassLoader();
        Class<?> klass;
        try {
            klass = classLoader.loadClass(className);
            return klass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void destroy() {
    }

}
