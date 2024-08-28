package com.ericsson.cifwk.taf.metrics.sample;

import com.google.common.base.Preconditions;

import java.net.URI;
import java.util.Date;

public final class Sample {

    private Long threadId;
    private String vuserId;
    private String executionId;
    private String testSuite;
    private String testCase;
    private Date eventTime;
    private String protocol;
    private URI target;
    private String requestType;
    private Long requestSize;
    private Boolean success;
    private Integer responseTime;
    private Integer latency;
    private Long responseSize;

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getVuserId() {
        return vuserId;
    }

    public void setVuserId(String vuserId) {
        this.vuserId = vuserId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getTestSuite() {
        return testSuite;
    }

    public void setTestSuite(String testSuite) {
        this.testSuite = testSuite;
    }

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public URI getTarget() {
        return target;
    }

    public void setTarget(URI target) {
        this.target = target;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public long getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(long requestSize) {
        this.requestSize = requestSize;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getLatency() {
        return latency;
    }

    public void setLatency(int latency) {
        this.latency = latency;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public static SampleBuilder builder() {
        return new SampleBuilder();
    }

    public void validate() {
        Preconditions.checkNotNull(threadId);
        Preconditions.checkNotNull(vuserId);
        Preconditions.checkNotNull(executionId);
        Preconditions.checkNotNull(testSuite);
        Preconditions.checkNotNull(testCase);
        Preconditions.checkNotNull(eventTime);
        Preconditions.checkNotNull(protocol);
        Preconditions.checkNotNull(target);
        Preconditions.checkNotNull(requestType);
        Preconditions.checkNotNull(requestSize);
        Preconditions.checkNotNull(success);
        Preconditions.checkNotNull(responseTime);
        Preconditions.checkNotNull(latency);
        Preconditions.checkNotNull(responseSize);
    }

}
