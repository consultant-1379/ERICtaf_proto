package com.ericsson.cifwk.taf.grid.runner;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.context.DefaultContextLoader;

import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public final class IsolatedContainer implements TestContainer {

    private final Logger logger = LoggerFactory.getLogger(IsolatedContainer.class);

    private JarClassLoader jarClassLoader;
    private DefaultContextLoader contextLoader;

    @Override
    public String getName() {
        return "isolated";
    }

    @Override
    public void init(String testware) {
        logger.info("Setting-up classpath with testware : {} ", testware);
        try {
            jarClassLoader = initializeClassLoader(testware);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private JarClassLoader initializeClassLoader(String testware) throws MalformedURLException {
        JarClassLoader jarClassLoader = new JarClassLoader();
        if (!Strings.isNullOrEmpty(testware)) {
            for (String url : testware.split(";")) {
                jarClassLoader.add(new URL(url));
            }
        }

        return jarClassLoader;
    }

    @Override
    public Object createInstance(String className) {
        JclObjectFactory factory = JclObjectFactory.getInstance();
        return factory.create(jarClassLoader, className);
    }

    @Override
    public void destroy() {
    }

}
