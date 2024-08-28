package com.ericsson.cifwk.taf.metrics.http;

import com.ericsson.cifwk.taf.performance.metric.TestContext;
import org.testng.*;

public class MetricsTestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        ITestContext context = result.getTestContext();
        TestContext.set(context);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        onTestFinished(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        onTestFinished(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        onTestFinished(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        onTestFinished(result);
    }

    private void onTestFinished(ITestResult result) {
        TestContext.reset();
    }

    @Override
    public void onStart(ITestContext context) {
    }

    @Override
    public void onFinish(ITestContext context) {
    }

}
