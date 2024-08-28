package com.ericsson.cifwk.taf.grid.web;

import com.ericsson.cifwk.taf.performance.metric.Metrics;

/**
 *
 */
public final class MetricsDto {

    public long timeMin;
    public long timeMax;
    public double timeMean;
    public double throughput;
    public long successCount;
    public long failureCount;
    public long skippedCount;
    public String successPercent;
    public String failurePercent;
    public String skippedPercent;
    public long total;

    public MetricsDto(Metrics metrics) {
        this.failureCount = metrics.getFailureCount();
        this.skippedCount = metrics.getSkippedCount();
        this.successPercent = "" + metrics.getSuccessPercent();
        this.successCount = metrics.getSuccessCount();
        this.failurePercent = "" + metrics.getFailurePercent();
        this.timeMax = metrics.getTimeMax();
        this.timeMin = metrics.getTimeMin();
        this.timeMean = metrics.getTimeMean();
        this.skippedPercent = "" + metrics.getSkippedPercent();
        this.throughput = metrics.getThroughput();
        this.total = metrics.getTotal();
    }
}
