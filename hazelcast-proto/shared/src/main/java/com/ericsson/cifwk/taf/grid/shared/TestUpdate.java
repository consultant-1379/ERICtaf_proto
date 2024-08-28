package com.ericsson.cifwk.taf.grid.shared;

import java.io.Serializable;

/**
 *
 */
public class TestUpdate implements Serializable {

    private String testName;
    private GridTestResult status;
    private long executionTime;
    private String stackTrace;

    public static TestUpdate ok(String name, long executionTime) {
        TestUpdate result = new TestUpdate();
        result.testName = name;
        result.status = GridTestResult.SUCCESS;
        result.executionTime = executionTime;
        return result;
    }

    public static TestUpdate fail(String name, String stackTrace) {
        TestUpdate result = new TestUpdate();
        result.testName = name;
        result.status = GridTestResult.FAILURE;
        result.stackTrace = stackTrace;
        return result;
    }

    public static TestUpdate error(String name, String stackTrace) {
        TestUpdate result = new TestUpdate();
        result.testName = name;
        result.status = GridTestResult.ERROR;
        result.stackTrace = stackTrace;
        return result;
    }

    public String getTestName() {
        return testName;
    }

    public GridTestResult getStatus() {
        return status;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    @Override
    public String toString() {
        return "TestUpdate{" +
                "testName='" + testName + '\'' +
                ", status=" + status +
                ", executionTime=" + executionTime +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestUpdate that = (TestUpdate) o;

        if (status != that.status) return false;
        if (testName != null ? !testName.equals(that.testName) : that.testName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = testName != null ? testName.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

}
