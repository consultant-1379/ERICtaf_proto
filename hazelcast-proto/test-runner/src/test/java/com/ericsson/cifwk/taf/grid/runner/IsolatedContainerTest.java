package com.ericsson.cifwk.taf.grid.runner;

import org.junit.Test;

/**
 *
 */
public class IsolatedContainerTest {

    @Test
    public void shouldHaveTwoContainers() {
        IsolatedContainer container1 = new IsolatedContainer();
        IsolatedContainer container2 = new IsolatedContainer();

        container1.init("");
        container2.init("");

        container1.createInstance("java.lang.String");
        container2.createInstance("java.lang.String");

        container1.destroy();
        container2.destroy();
    }

}
