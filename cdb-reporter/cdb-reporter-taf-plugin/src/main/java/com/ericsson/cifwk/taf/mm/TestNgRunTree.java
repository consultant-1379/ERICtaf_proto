package com.ericsson.cifwk.taf.mm;

import java.util.Map;
import java.util.Set;

import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestResult;

import com.ericsson.cifwk.taf.annotations.TestId;

public class TestNgRunTree extends RunTree {

    public TestNgRunTree(String runName) {
        super(runName);
    }

    protected void updateMainNodeColor(Status suiteStatus) {
        if (suiteStatus == Status.PASSED) {
            if (WHITE.equals(mainNodeColor)) {
                mainNodeColor = GREEN;
            }
        }
        if (suiteStatus == Status.FAILED) {
            mainNodeColor = RED;
        }
        if (suiteStatus == Status.SKIPPED) {
            if(!mainNodeColor.equals(RED)){
                mainNodeColor = YELLOW;
            }
        }
    }

    private String buildName(ITestResult testMethod) {
        // String id = JcatModelHolder.getCurrentTestCase().getTestCaseId();
        // String heading =
        // JcatModelHolder.getCurrentTestCase().getTestCaseId();

        String name = null;

        // if (id != null && heading != null && !id.equals("N/A")
        // && !heading.equals("null")) {
        // name = id + " " + heading;
        // } else {
        TestId testId = testMethod.getMethod().getConstructorOrMethod()
                .getMethod().getAnnotation(TestId.class);
        if (testId != null) {
            name = testId.id() + " " + testId.title();
        } else {
            name = testMethod.getName();
        }
        // }
        return name;
    }

    private void makeNodes(Set<ITestResult> results, Status status,
            StringBuilder bufferToAppendTo) {
        for (ITestResult result : results) {
            bufferToAppendTo.append(makeNode(buildName(result),
                    getColor(status), true));
        }
    };

    public void addSuite(ISuite suite) {
        Status suiteStatus = Status.PASSED;

        StringBuilder suiteString = new StringBuilder();
        StringBuilder children = new StringBuilder();
        Map<String, ISuiteResult> testResults = suite.getResults();
        for (String testName : testResults.keySet()) {
            makeNodes(testResults.get(testName).getTestContext()
                    .getPassedTests().getAllResults(), Status.PASSED, children);
            Set<ITestResult> skips = testResults.get(testName).getTestContext()
                    .getSkippedTests().getAllResults();
            if (skips.size() > 0) {
                makeNodes(skips, Status.SKIPPED, children);
                suiteStatus = Status.FAILED;
            }
            Set<ITestResult> failures = testResults.get(testName)
                    .getTestContext().getFailedTests().getAllResults();
            if (failures.size() > 0) {
                makeNodes(failures, Status.FAILED, children);
                suiteStatus = Status.FAILED;
            }
        }
        suiteString.append(makeNode(suite.getName(), getColor(suiteStatus),
                false));
        suiteString.append(children);
        suiteString.append(NODE_CLOSE);
        updateMainNodeColor(suiteStatus);
        allSuites += suiteString.toString();
    }

}
