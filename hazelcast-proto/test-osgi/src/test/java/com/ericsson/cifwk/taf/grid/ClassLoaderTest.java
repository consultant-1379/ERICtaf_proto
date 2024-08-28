package com.ericsson.cifwk.taf.grid;

import org.junit.Ignore;
import org.junit.Test;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.net.URL;

/**
 *
 */
public class ClassLoaderTest {

    public static final String CLASS_NAME = "com.ericsson.cifwk.taf.grid.sample.SampleTestCase";

    @Test
    @Ignore
    public void shouldLoad() throws Exception {
        JarClassLoader jarClassLoader = new JarClassLoader();
        jarClassLoader.add(new URL("http://127.0.0.1:9090/api/jar/com.ericsson.cifwk.taf.grid/testware/1.0.0-SNAPSHOT/.jar"));

//        DefaultContextLoader contextLoader = new DefaultContextLoader(jarClassLoader);
//        contextLoader.loadContext();

        JclObjectFactory factory = JclObjectFactory.getInstance();
        Object instance = factory.create(jarClassLoader, CLASS_NAME);

        Class type = jarClassLoader.loadClass(CLASS_NAME);
        System.out.println(type);
    }

}
