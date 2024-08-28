package com.ericsson.cifwk.taf.grid.shared;

import java.io.Serializable;

/**
 *
 */
public final class TestSpecification implements Serializable {

    private String runner;
    private String testware;
    private String container;
    private TestStep[] testSteps;

    public TestSpecification() {
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    public void setTestware(String testware) {
        this.testware = testware;
    }

    public void setTestSteps(TestStep[] testSteps) {
        this.testSteps = testSteps;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getRunner() {
        return runner;
    }

    public String getTestware() {
        return testware;
    }

    public TestStep[] getTestSteps() {
        return testSteps;
    }

}
